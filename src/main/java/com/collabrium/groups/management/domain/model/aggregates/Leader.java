package com.collabrium.groups.management.domain.model.aggregates;

import com.collabrium.groups.management.domain.exceptions.InvalidLeaderOperationException;
import com.collabrium.groups.management.domain.exceptions.LeaderConsistencyException;
import com.collabrium.groups.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Duration;

@Entity
@Table(name = "leaders")
@Getter
public class Leader extends AuditableAbstractAggregateRoot<Leader> {

  @Column(name = "average_solution_time", nullable = false)
  @JdbcTypeCode(SqlTypes.INTERVAL_SECOND)
  private Duration averageSolutionTime;

  @Column(name = "solved_requests", nullable = false)
  private Integer solvedRequests;

  public Leader() {
    this.solvedRequests = 0;
    this.averageSolutionTime = Duration.ZERO;
  }

  public void incrementSolvedRequests() {
    validateSolvedRequestsState();
    this.solvedRequests++;
    recalculateAverageSolutionTime();
  }

  public void addSolutionTime(Duration solutionDuration) {
    validateSolutionDuration(solutionDuration);
    validateSolvedRequestsState();

    try {
      long currentTotalSeconds = calculateCurrentTotalSeconds();

      long newTotalSeconds =
          currentTotalSeconds + solutionDuration.getSeconds();

      int newSolvedRequests = this.solvedRequests + 1;

      long newAverageSeconds =
          newTotalSeconds / newSolvedRequests;

      this.solvedRequests = newSolvedRequests;

      this.averageSolutionTime =
          Duration.ofSeconds(newAverageSeconds);

    } catch (ArithmeticException e) {
      throw new LeaderConsistencyException(e.getMessage());
    }
  }

  private void validateSolutionDuration(Duration solutionDuration) {
    if (solutionDuration == null) {
      throw InvalidLeaderOperationException.forNullSolutionDuration();
    }

    if (solutionDuration.isNegative()) {
      throw InvalidLeaderOperationException
          .forNegativeSolutionDuration(solutionDuration.getSeconds());
    }
  }

  private void validateSolvedRequestsState() {
    if (solvedRequests == null) {
      this.solvedRequests = 0;
      this.averageSolutionTime = Duration.ZERO;
    }

    if (solvedRequests < 0) {
      throw InvalidLeaderOperationException
          .forInvalidSolvedRequests(solvedRequests);
    }
  }

  private long calculateCurrentTotalSeconds() {

    if (solvedRequests == 0) {
      return 0;
    }

    long averageSeconds =
        averageSolutionTime != null
            ? averageSolutionTime.getSeconds()
            : 0;

    if (averageSeconds == 0 && solvedRequests > 0) {
      throw LeaderConsistencyException
          .forMismatchBetweenRequestsAndTime();
    }

    return averageSeconds * solvedRequests;
  }

  private void recalculateAverageSolutionTime() {
    if (solvedRequests == 0) {
      this.averageSolutionTime = Duration.ZERO;
    }
  }

  public void resetMetrics() {
    this.solvedRequests = 0;
    this.averageSolutionTime = Duration.ZERO;
  }

  public boolean hasSolvedRequests() {
    return solvedRequests != null && solvedRequests > 0;
  }

  public long getAverageSolutionTimeInSeconds() {
    if (averageSolutionTime == null) {
      return 0;
    }

    return averageSolutionTime.getSeconds();
  }

  public double getAverageSolutionTimeInMinutes() {
    return getAverageSolutionTimeInSeconds() / 60.0;
  }

  public String getFormattedAverageSolutionTime() {

    if (averageSolutionTime == null) {
      return "00:00:00";
    }

    long totalSeconds = averageSolutionTime.getSeconds();

    long hours = totalSeconds / 3600;
    long minutes = (totalSeconds % 3600) / 60;
    long seconds = totalSeconds % 60;

    return String.format(
        "%02d:%02d:%02d",
        hours,
        minutes,
        seconds
    );
  }

  public double getSuccessRate() {
    if (solvedRequests == null || solvedRequests == 0) {
      return 0.0;
    }

    return 100.0;
  }

  @Override
  public String toString() {
    return String.format(
        "Leader{id=%d, solvedRequests=%d, averageSolutionTime=%s}",
        getId() != null ? getId() : 0,
        solvedRequests != null ? solvedRequests : 0,
        getFormattedAverageSolutionTime()
    );
  }
}
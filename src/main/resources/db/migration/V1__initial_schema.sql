CREATE TABLE leaders (
                         id BIGSERIAL PRIMARY KEY,

                         average_solution_time INTERVAL NOT NULL DEFAULT INTERVAL '0 seconds',
                         solved_requests INTEGER NOT NULL DEFAULT 0,

                         created_at TIMESTAMP NOT NULL DEFAULT now(),
                         updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE groups (
                        id BIGSERIAL PRIMARY KEY,

                        code VARCHAR(100) NOT NULL UNIQUE,
                        name VARCHAR(255) NOT NULL,
                        description TEXT NOT NULL,
                        img_url VARCHAR(500),

                        leader_id BIGINT NOT NULL,

                        member_count INTEGER NOT NULL DEFAULT 0,

                        created_at TIMESTAMP NOT NULL DEFAULT now(),
                        updated_at TIMESTAMP NOT NULL DEFAULT now(),

                        CONSTRAINT fk_groups_leader
                            FOREIGN KEY (leader_id)
                                REFERENCES leaders (id)
                                ON DELETE RESTRICT
);

CREATE TABLE invitations (
                             id BIGSERIAL PRIMARY KEY,

                             member_id BIGINT NOT NULL,
                             group_id BIGINT NOT NULL,

                             created_at TIMESTAMP NOT NULL DEFAULT now(),
                             updated_at TIMESTAMP NOT NULL DEFAULT now(),

                             CONSTRAINT fk_invitations_group
                                 FOREIGN KEY (group_id)
                                     REFERENCES groups (id)
                                     ON DELETE CASCADE
);

CREATE INDEX idx_groups_code ON groups(code);
CREATE INDEX idx_groups_leader_id ON groups(leader_id);

CREATE INDEX idx_invitations_member_id ON invitations(member_id);
CREATE INDEX idx_invitations_group_id ON invitations(group_id);
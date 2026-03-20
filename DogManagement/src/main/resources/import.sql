-- População inicial do DogManagement (15 cães)
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

INSERT INTO dog (id, name, race, status, sex, age, created_at, updated_at) VALUES (gen_random_uuid(), 'Rex', 'GOLDEN_RETRIEVER', 'SOCIALIZACAO', 'M', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO dog (id, name, race, status, sex, age, created_at, updated_at) VALUES (gen_random_uuid(), 'Luna', 'Labrador', 'TREINAMENTO', 'F', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO dog (id, name, race, status, sex, age, created_at, updated_at) VALUES (gen_random_uuid(), 'Thor', 'PASTOR_ALEMAO', 'PRE_SOCIALIZACAO', 'M', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO dog (id, name, race, status, sex, age, created_at, updated_at) VALUES (gen_random_uuid(), 'Bella', 'Border_Collie', 'ADAPTACAO', 'F', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO dog (id, name, race, status, sex, age, created_at, updated_at) VALUES (gen_random_uuid(), 'Max', 'PASTOR_ALEMAO', 'CEDIDO', 'M', 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO dog (id, name, race, status, sex, age, created_at, updated_at) VALUES (gen_random_uuid(), 'Mel', 'GOLDEN_RETRIEVER', 'DOACAO', 'F', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO dog (id, name, race, status, sex, age, created_at, updated_at) VALUES (gen_random_uuid(), 'Apollo', 'Labrador', 'TREINAMENTO', 'M', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO dog (id, name, race, status, sex, age, created_at, updated_at) VALUES (gen_random_uuid(), 'Pandora', 'Border_Collie', 'SOCIALIZACAO', 'F', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO dog (id, name, race, status, sex, age, created_at, updated_at) VALUES (gen_random_uuid(), 'Zeus', 'PASTOR_ALEMAO', 'TREINAMENTO', 'M', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO dog (id, name, race, status, sex, age, created_at, updated_at) VALUES (gen_random_uuid(), 'Maya', 'GOLDEN_RETRIEVER', 'ADAPTACAO', 'F', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO dog (id, name, race, status, sex, age, created_at, updated_at) VALUES (gen_random_uuid(), 'Billy', 'Labrador', 'PRE_SOCIALIZACAO', 'M', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO dog (id, name, race, status, sex, age, created_at, updated_at) VALUES (gen_random_uuid(), 'Nina', 'Border_Collie', 'SOCIALIZACAO', 'F', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO dog (id, name, race, status, sex, age, created_at, updated_at) VALUES (gen_random_uuid(), 'Bento', 'GOLDEN_RETRIEVER', 'TREINAMENTO', 'M', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO dog (id, name, race, status, sex, age, created_at, updated_at) VALUES (gen_random_uuid(), 'Kyra', 'PASTOR_ALEMAO', 'ADAPTACAO', 'F', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO dog (id, name, race, status, sex, age, created_at, updated_at) VALUES (gen_random_uuid(), 'Dante', 'Labrador', 'CEDIDO', 'M', 7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
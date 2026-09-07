-- População inicial do DogTraining
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

INSERT INTO training (id, trainer_id, dog_id, dogs_name, dogs_breed, month_year, day, evaluation_type, current_stage, status)
VALUES
    (gen_random_uuid(), gen_random_uuid(), gen_random_uuid(), 'Rex', 'GOLDEN_RETRIEVER', 202609, 1, 'Avaliação de Obediência', 1, NULL),
    (gen_random_uuid(), gen_random_uuid(), gen_random_uuid(), 'Luna', 'Labrador', 202609, 2, 'Comportamento Urbano', 2, NULL),
    (gen_random_uuid(), gen_random_uuid(), gen_random_uuid(), 'Thor', 'PASTOR_ALEMAO', 202609, 3, 'Guarda e Proteção', 1, NULL),
    (gen_random_uuid(), gen_random_uuid(), gen_random_uuid(), 'Bella', 'Border_Collie', 202609, 4, 'Agility Inicial', 3, NULL),
    (gen_random_uuid(), gen_random_uuid(), gen_random_uuid(), 'Max', 'PASTOR_ALEMAO', 202609, 5, 'Socialização', 4, NULL),
    (gen_random_uuid(), gen_random_uuid(), gen_random_uuid(), 'Mel', 'GOLDEN_RETRIEVER', 202609, 6, 'Guia Básico', 1, NULL),
    (gen_random_uuid(), gen_random_uuid(), gen_random_uuid(), 'Apollo', 'Labrador', 202609, 7, 'Detecção de Odores', 2, NULL),
    (gen_random_uuid(), gen_random_uuid(), gen_random_uuid(), 'Pandora', 'Border_Collie', 202609, 8, 'Pastoril Básico', 1, NULL),
    (gen_random_uuid(), gen_random_uuid(), gen_random_uuid(), 'Zeus', 'PASTOR_ALEMAO', 202609, 9, 'Obediência Avançada', 3, NULL),
    (gen_random_uuid(), gen_random_uuid(), gen_random_uuid(), 'Maya', 'GOLDEN_RETRIEVER', 202609, 10, 'Assistência Inicial', 2, NULL),
    (gen_random_uuid(), gen_random_uuid(), gen_random_uuid(), 'Billy', 'Labrador', 202609, 11, 'Recuperação de Objetos', 1, NULL),
    (gen_random_uuid(), gen_random_uuid(), gen_random_uuid(), 'Nina', 'Border_Collie', 202609, 12, 'Comandos por Gestos', 2, NULL),
    (gen_random_uuid(), gen_random_uuid(), gen_random_uuid(), 'Bento', 'GOLDEN_RETRIEVER', 202609, 13, 'Acompanhamento', 3, NULL),
    (gen_random_uuid(), gen_random_uuid(), gen_random_uuid(), 'Kyra', 'PASTOR_ALEMAO', 202609, 14, 'Guarda Territorial', 4, NULL),
    (gen_random_uuid(), gen_random_uuid(), gen_random_uuid(), 'Dante', 'Labrador', 202609, 15, 'Trabalho de Faro', 4, 'Apto');
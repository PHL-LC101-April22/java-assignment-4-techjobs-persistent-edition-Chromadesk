-- Part 1: Test it with SQL
-- id = int PRIMARY_KEY, employer = varchar, name = varchar, skills = varchar
-- Part 2: Test it with SQL
SELECT name FROM techjobs.employer
WHERE employer.location = "St. Louis City";
-- Part 3: Test it with SQL
DROP TABLE job;
-- Part 4: Test it with SQL
SELECT skill.name
FROM techjobs.job_skills
INNER JOIN skill ON job_skills.skills_id = skill.id
ORDER BY skill.name ASC
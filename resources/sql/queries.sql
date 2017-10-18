-- :name create-user! :! :n
-- :doc creates a new user record
INSERT INTO users
(username, display_name, email)
VALUES (:username, :display_name, :email)

-- :name update-user! :! :n
-- :doc update an existing user record
UPDATE users
SET display_name = :display_name, email = :email
WHERE username = :username

-- :name get-user :? :1
-- :doc retrieve a user given the id.
SELECT * FROM users
WHERE username = :username

-- :name search-user :? :*
-- :doc search for users with a username or display name containing :term
SELECT * FROM users
WHERE username LIKE :term OR display_name LIKE :term

-- :name delete-user! :! :n
-- :doc delete a user given the id
DELETE FROM users
WHERE username = :username

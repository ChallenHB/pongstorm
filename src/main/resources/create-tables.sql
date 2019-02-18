-- app's user information user-name: pongstorm_user  pw:pongstorm_user

-- Separate User from Player

CREATE TABLE players (
  id int PRIMARY KEY,
  rating float NOT NULL,
  volatility float NOT NULL,
  deviation float NOT NULL
);

CREATE TYPE outcome_type AS ENUM ('one', 'two', 'draw');

CREATE TABLE game_outcomes (
  id int PRIMARY KEY,
  player_one_id int REFERENCES players (id),
  player_two_id int REFERENCES players (id),
  winner outcome_type NOT NULL
)

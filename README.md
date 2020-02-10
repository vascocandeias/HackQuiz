# HackQuiz

Android application where two players face each other to answer trivia questions. The player that gets the most correct answers gets a reward from a candy machine. In case of a tie, the player with the fastest answers wins.

The system is centralized, with a Raspberry Pi acting as a server where a REST API (made with Flask) is consumed to POST the players' informations and scores, and to GET the set of questions and the winner. The machine consists of an Arduino with a Wi-Fi module which, after retrieving the winner from de API, uses a servo motor to dispense candy to the right player.

The main repo can be found [here](https://github.com/leandroljpa97/CandyMachine "CandyMachine repo").

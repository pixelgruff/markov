# markov

Codename "Markov" is a project aimed at creating an extremely accessible framework for building and testing game-playing Artificial Intelligence. What games, you ask? Any game that can be expressed in terms of States, Actions with a concrete Rule definition governing the relationship between the two.

## Current Prerequisites
---
* A development environment that supports JDK8. We make heavy use of lambdas throughout the code.

## Getting Started
---
To get started, just check out this repo! We provide an Eclipse project file that is checked in and kept up-to-date with each release.
* [Load the project in IntelliJ](https://www.jetbrains.com/idea/webhelp10.5/importing-eclipse-project-to-intellij-idea.html)
* [Load the project in Netbeans](http://docs.oracle.com/cd/E50453_01/doc.80/e50452/create_japps.htm#NBDAG445)

Once downloaded, you should simply be able to run one of the provided "Test" game files under any of the game directories. Check out the [Currently Supported Games] section for an exhaustive list of the games that we currently support. These Test classes provide the bare minimum of what is necessary to wire up code to a game and have it run. Running them will grab an AI that moves randomly and have it it play out the game, or a collection of AIs that will play the game against each other if its adversarial / multiplayer.

## Core Concepts
---
In order to provide a framework for arbitrary games, we've come up with a core set of hefty abstractions. They are State, Action, Rules, and Policy

### State
---
States are a simple concept at heart. They represent the state of a game. Easy, right? What does this mean? For TicTacToe, it would be exactly which boxes on the TicTacToe board have X's, which ones have O's, the player who can currently take an action, and whether or not the gameboard represents a finished game. For monopoly, it would be who owns what property, who's turn it is, where everyone's pieces are on the board, the board, which player has what amount of money, how many houses are left, etc. 

All of the above examples are talking about global state. Or, in different terms, the "true" state of the game. That's great, but we mentioned earlier that this is a framework for arbitrary games. What about games that are based around hidden information, like Poker, or Stratego? How do we cope with that if state is global? Well, that's easy! We "hide" state, making it not global. At least, in the eyes of the player. This concept will be explained later on in the Rules abstraction, so don't worry about it too much for now.

Simply put, the State abstraction is a representation of the state of the game. Since all games do not share any real common elements (some have global state, some have player-specific state), there is no common class - States are defined on a per-game basis.

### Rules
---


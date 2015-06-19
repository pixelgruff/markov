# markov

Codename "Markov" is a project aimed at creating an extremely accessible framework for building and testing game-playing Artificial Intelligence. What games, you ask? Any game that can be expressed in terms of States, Actions with a concrete Rule definition governing the relationship between the two.

## Goals
Our end goal is to have a website where users can submit AIs into tournaments, group competitions, or regularly scheduled matches. *All of these matches will be run on our own hardware, so there is no need for users to download the full software, schedule times, or connect to weird servers. This will all be handled by us.* We'll focus on popular requests for two things
* Language endpoints for specific games
* More games

To expand on these: In order for this framework to be cross-language compatible, we must provide client / core implementations of each game for each language. This is time consuming, so for pre-pre-alpha, which we're currently in, we're focusing our efforts on Java only. Additionally, games must be implemented, both in java and in any other target language such that choosing a language does not give a user an unfair advantage. Thus, care must be taken to ensure that all components of each game are well-crafted.

## Current progress
* Fleshed-out Game-Playing API
* Full TicTacToe implementation
* Prototype networked game server, communicating between clients via JSON

### In-development
* Wumpus World
* Tetris

## Lacking / In-Progress soon
* Pretty pretty views. These will be focused on later, as part of the web framework, and written in javascript. We will not provide pretty pretty views for locally played games, only text output.
* Website!
* Scripts to trigger game plays via the website

## Current Prerequisites

* JDK 1.8. We make heavy use of lambdas throughout the code.

## Getting Started

To get started, just check out this repo! We provide an Eclipse project file that is checked in and kept up-to-date with each release.
* [Load the project in IntelliJ](https://www.jetbrains.com/idea/webhelp10.5/importing-eclipse-project-to-intellij-idea.html)
* [Load the project in Netbeans](http://docs.oracle.com/cd/E50453_01/doc.80/e50452/create_japps.htm#NBDAG445)

Once downloaded, you should simply be able to run one of the provided "Test" game files under any of the game directories. Check out the [Currently Supported Games] section for an exhaustive list of the games that we currently support. These Test classes provide the bare minimum of what is necessary to wire up code to a game and have it run. Running them will grab an AI that moves randomly and have it it play out the game, or a collection of AIs that will play the game against each other if its adversarial / multiplayer.

## Core Concepts

In order to provide a framework for arbitrary games, we've come up with a core set of hefty abstractions. They are State, Action, Rules, and Policy

### State

States are a simple concept at heart. They represent the state of a game. Easy, right? But what does this mean? For TicTacToe, it would be exactly which boxes on the TicTacToe board have X's, which ones have O's, the player who can currently take an action, and whether or not the gameboard represents a finished game. For monopoly, it would be who owns what property, who's turn it is, where everyone's pieces are on the board, the board, which player has what amount of money, how many houses are left, etc. 

All of the above examples are talking about global state. Or, in different terms, the "true" state of the game. That's great, but we mentioned earlier that this is a framework for arbitrary games. What about games that are based around hidden information, like Poker, or Stratego? How do we cope with that if state is global? Well, that's easy! We "hide" state, making it not global. At least, in the eyes of the player. This concept will be explained later on in the Rules abstraction, so don't worry about it too much for now.

Simply put, the State abstraction is a representation of the state of the game. Since all games do not share any real common elements (some have global state, some have player-specific state), there is no common class - States are defined on a per-game basis.

### Rules

[Rules](https://github.com/mtl-stepchild/markov/blob/master/src/core/Rules.java) are at the very core of every game. For us, the Rules abstraction *defines* the game. A Rules object has a number of useful features. They provide a way to briding the gap between States, Actions, and Policies. Rules will give us the initial setup for a game, define what happens if a certain action is taken at a certain state, determine how many players can play the game, determine the current player for any state, and even turn states into what it appears to be through the player's eyes (hidden information games). Rules do all of this.

Rules and games are incredibly similar concepts, but we only have a Rules class in our codebase, and no Game class. Why is this? After much discussion, we determined that Rules and State are completely separate entities. One can play poker with a variety of rules that are not necessarily tied to the current state of the game. In order to do this without separating the Rules and State, we'd have to duplicate a *lot* of logic. However, with a "house rules" mentality, we can easily create a different instance of the Rules, substitute that in to our game, and carry on our merry way. 

Rules offer a clean interface for "querying" gamestate.

### Policies

[Policies](https://github.com/mtl-stepchild/markov/blob/master/src/core/Policy.java) are an incredibly simple interface consisting of only a single function: choose an Action from an enumeration of available Actions for a State. That's it. It is from this that AI are built. What goes into that logic is entirely up to you, as a user of this framework. We provide some simple AI as examples.

All you need to do to create an AI is create a class that implements the Policy interface. Here's a simple TicTacToe Policy implementation as an example:
```Java
public class SimpleJohnnyTicTacToePolicy implements Policy<TicTacToeState, TicTacToeAction>
{
    @Override
    public TicTacToeAction chooseAction(TicTacToeState currentState, Collection<TicTacToeAction> actions)
    {
        Vector2 center = new Vector2(1, 1);
        for(TicTacToeAction availableAction : actions) {
            /* If the center of the board is available, take it */
            if(center.equals(availableAction.getPosition)) {
                return availableAction;
            }
        }
        /* Otherwise, who cares, pick a random spot */
        List<TicTacToeAction> actionsAsList = new ArrayLis<>(actions);
        int chosenIndex = ThreadLocalRandom.current().nextInt(actionsAsList.size());
        return actionsAsList.get(chosenIndex);
    }
}
```
That's it! That's all there is to it. Keep in mind that the actions returned from this function are validated, so you returning an action that was not available to you will cause *consequences*.

### Actions

Actions are the "moves" available to a Policy at any given stage of the game. They are enumerated via the Rules for a given State. This enumeration is presented to a Policy, from which it determines what move to choose. For example, in TicTacToe, an action consists of a position on the board and a mark (X or O) to put on that position. Simple stuff.

## Automator
[Automators](https://github.com/mtl-stepchild/markov/blob/master/src/core/Automator.java) ...automate games. They'll play the game for you! Give it some policies and off it goes. We currently have a [LocalAutomator](https://github.com/mtl-stepchild/markov/blob/master/src/core/automators/LocalAutomator.java) which will play games in memory (Java-only policies) and are have a [NetworkedAutomator](https://github.com/wallstop/MarkovNetwork/blob/master/src/core/network/NetworkAutomator.java) in the works will allow for cross-language game automation (arbitrary language policies)


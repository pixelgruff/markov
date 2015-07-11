package core.policies;

import core.Policy;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Allow a user to play the game with a simple toString() update loop and
 * console input.
 *
 * @author Ginger
 * @param <S>
 * @param <A>
 */
public class PrintPolicy<S, A> implements Policy<S, A> {

    private final Map<String, A> actionMap_ = new HashMap<>();
    private final Scanner scan = new Scanner(System.in);

    public PrintPolicy(final Map<String, A> actionMap) {
        /* Maintain a mapping of console commands to actions */
        actionMap_.putAll(actionMap);
    }

    @Override
    public A chooseAction(S state, Collection<A> actions) {
        System.out.printf("%s\n", state.toString());
        final StringBuilder builder = new StringBuilder();
        actions.stream().forEach((action) -> {
            builder.append(String.format("%s (%s) ",
                    action.toString(),
                    /* Reverse the map to find the keystroke that matches this action */
                    actionMap_.keySet().stream()
                    .filter((key) -> actionMap_.get(key) == action)
                    .findAny().get()));
        });
        builder.append(System.lineSeparator());
        builder.append("$ ");
        System.out.print(builder.toString());

        /* Allow the user to choose the action */
        while (true) {
            String input = scan.nextLine();
            if (!actionMap_.containsKey(input)) {
                System.err.println("No such command!");
            } else if (!actions.contains(actionMap_.get(input))) {
                System.err.println("Action not valid for this state!");
            } else {
                return actionMap_.get(input);
            }
        }
    }
}

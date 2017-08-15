package edu.kit.formal.proofscriptparser.types;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Weigl
 * @version 1 (15.08.17)
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TermType implements Type {
    private List<Type> argTypes = new ArrayList<>();

    @Override
    public String symbol() {
        return "Term<" +
                argTypes.stream().map(Type::symbol).reduce((a, b) -> (a + "," + b)).orElse("")
                + ">";
    }

}

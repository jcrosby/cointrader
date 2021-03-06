package org.cryptocoinpartners.bin;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import org.cryptocoinpartners.module.Context;


/**
 * @author Tim Olson
 */
@SuppressWarnings("UnusedDeclaration")
@Parameters(commandNames = "backtest", commandDescription = "backtest a strategy (not functional)")
public class BacktestRunMode extends RunMode {

    @Parameter(description = "Strategy name to load", arity = 1, required = true)
    public String strategyName;

    public void run() {
        Context context = Context.create();
        // todo set time manager
        // todo load data producing module
    }

}

package com.liferay.cli.shell;

import java.util.List;

public interface Tailor {

    /**
     * Transforms input command using available Tailor implementation and
     * activated configuration.
     *
     * @param command - ray command line
     * @return - adjusted command or list of commands. empty list if command is
     *         not tailored
     */
    List<String> sew(String command);
}

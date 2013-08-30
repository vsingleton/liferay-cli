package com.liferay.cli.project;

import java.io.File;

import org.apache.commons.lang3.Validate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import com.liferay.cli.file.undo.FilenameResolver;
import com.liferay.cli.support.util.FileUtils;

/**
 * {@link FilenameResolver} that delegates to {@link PathResolver}.
 * 
 * @author Ben Alex
 * @since 1.0
 */
@Component(immediate = true)
@Service
public class PathResolvingAwareFilenameResolver implements FilenameResolver {

    @Reference private PathResolver pathResolver;

    public String getMeaningfulName(final File file) {
        Validate.notNull(file, "File required");
        return pathResolver.getFriendlyName(FileUtils.getCanonicalPath(file));
    }
}

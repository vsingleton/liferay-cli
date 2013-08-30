package com.liferay.cli.project.packaging;

import com.liferay.cli.project.packaging.PackagingProvider;
import com.liferay.cli.project.packaging.PomPackaging;

/**
 * Unit test of the {@link PomPackaging} {@link PackagingProvider}
 * 
 * @author Andrew Swan
 * @since 1.2.0
 */
public class PomPackagingTest extends PackagingProviderTestCase<PomPackaging> {

    @Override
    protected PomPackaging getProvider() {
        return new PomPackaging();
    }
}

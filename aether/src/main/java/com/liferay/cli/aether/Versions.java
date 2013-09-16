package com.liferay.cli.aether;

import java.util.List;

import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.VersionRangeRequest;
import org.eclipse.aether.resolution.VersionRangeResult;
import org.eclipse.aether.version.Version;

public class Versions
{

    public static String getAvailable( String group, String artifact ) throws Exception {
    	
    	String available = "";

    	RepositorySystem repositorySystem = Repositories.newRepositorySystem();
    	if (repositorySystem == null) {
			System.err.println("main: repoSystem == null");
		} else {

			RepositorySystemSession repositorySystemSession = Repositories.newSession(repositorySystem);

			Artifact defaultArtifact = new DefaultArtifact(group + ":" + artifact + ":" + "[0,)");

			RemoteRepository remoteRepository = new RemoteRepository.Builder("central", "default",
					"http://repo1.maven.org/maven2/").build();

			VersionRangeRequest rangeRequest = new VersionRangeRequest();
			rangeRequest.setArtifact(defaultArtifact);
			rangeRequest.addRepository(remoteRepository);

			VersionRangeResult versionRangeResult = repositorySystem.resolveVersionRange(repositorySystemSession, rangeRequest);

			List<Version> versions = versionRangeResult.getVersions();
			if (versions == null) {
				System.out.println("getAvailable: newestVersion == null");
			} else {
				System.out.println("getAvailable: versions " + versions);
				available = versions.toString();
			}
		}
    	return available;
    }
    
    public static String getLatestMinor( String group, String artifact, String major1and2) throws Exception {
    	
    	String version = "";

    	RepositorySystem repositorySystem = Repositories.newRepositorySystem();
    	if (repositorySystem == null) {
			System.err.println("main: repoSystem == null");
		} else {

			RepositorySystemSession repositorySystemSession = Repositories.newSession(repositorySystem);

			Artifact defaultArtifact = new DefaultArtifact(group + ":" + artifact + ":" + "[0,)");

			RemoteRepository remoteRepository = new RemoteRepository.Builder("central", "default",
					"http://repo1.maven.org/maven2/").build();

			VersionRangeRequest rangeRequest = new VersionRangeRequest();
			rangeRequest.setArtifact(defaultArtifact);
			rangeRequest.addRepository(remoteRepository);

			VersionRangeResult versionRangeResult = repositorySystem.resolveVersionRange(repositorySystemSession, rangeRequest);

			Version highestVersion = versionRangeResult.getHighestVersion();
			
			if (highestVersion == null) {
				System.out.println("getLatestMinor: newestVersion == null");
			} else {
				System.out.println("getLatestMinor: highestVersion = " + highestVersion);
				version = highestVersion.toString();
			}
		}
    	
    	return version;
    }

}
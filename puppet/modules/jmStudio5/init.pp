class jmStudio2 {
	include ant
	
	class { 'jenkins' :
		executors => 1,
		version => '1.651.3-1.1';
	}
	
	jenkins::plugin {
		'ansicolor' :
			version => '0.0.1';
		'cloudbees-folder' :
			version => '5.13',
			enabled => false;
		'swarm' :
			version => '2.2';
		'build-environment' :
			version => '1.6';
	}
	
	jenkins::job {
		'build' : 
			config => '<?xml version=\'1.0\' encoding=\'UTF-8\'>
			<project>
			  <actions/>
			  <description></description>
			  <keepDependencies>false</keepDependencies>
			  <properties></properties>
			  <scm class="hudson.scm.NullSCM"/>
			  <canRoam>true</canRoam>
			  <disabled>false</disabled>
			  <blockBuildWhenDownstreamBuilding>false</blockBuildWhenDownstreamBuilding>
			  <blockBuildWhenUpstreamBuilding>false</blockBuildWhenUpstreamBuilding>
			  <triggers/>
			  <concurrentbuild>false</concurrentbuild>
			  <builders/>
			  <publishers/>
			  <buildWrappers/>
			</project>';
	}
}
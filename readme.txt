This repository is a clone of the repository for the first exercise, and I have used to deposit all the deliverables for the test.

For the first exercise, you can find the Unit test in src\test\java\org\gradle\examples\web\ServletTest.java.
Modifications to the build.gradle file have been done directly on the cloned one, that can be found at the root of the repository.
The Jenkinsfile that defines the job can be found next to it.

For the second exercise, I created a freestyle job that ran a system groovy script. Said script can be found at the root of the repository, and it's called cleanup.groovy
The script grabs the workspaces for all the jobs both in the master and in the slaves, for the jobs that are not running. 
It then sorts them out on last modified (the reasoning being, that I think repositories that haven't had activity are less likely to become active).
Finally, it deletes the older ones, leaving the 3 newest untouched.
The problems with this approach are that we are forced to resync if a job gets triggered after a while, and that weird things could happen if a job triggers between the collection of the workspace, and its deletion.
The second problem can become worse the bigger the Jenkins infrastructure, since it'll take more time between collection and deletion, but for a three-slave setting, with 10 jobs, the probability is quite small.
As a side note, on the three slave scenario, I would personally tag the slaves to prevent any of them from having the 10 repositories synced at the same time.

Finally, for the third exercise, I decided to use puppet. 
I have created five classes, one for each of the studios. They are very similar, but I hope you get what I am trying to do.
I then assign each of the Jenkins machines for each studio, its correspondent class, the filtering being the hostname of the jenkins master for the studio (assuming jenkins.studio1.com, jenkins.studio2.com, etc.).
The configuration files for this are in the puppet folder of the repository, and follow the trees of the /etc/puppet folder of the master.
This could be further improved by adding a Jenkins job that would use Vagrant to spawn a prepared Linux machine, containing the puppet agent, with the configuration pointing to the master, to speed up the process.
I, sadly, was unable to test this since I had several issues getting my VirtualBox VMs setup to actually work, and have an agent talk to a master.
The way the different studios could tinker with the settings would be to have those .pp files in a repository, and have, for instance, a Jenkins job, sync them every once in a while, in the puppet master.
The puppet master configuration files would probably have to be changed to include the jenkins workspace folder so as to keep those files updated.
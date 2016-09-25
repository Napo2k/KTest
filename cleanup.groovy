import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.attribute.BasicFileAttributes
import hudson.node_monitors.*;
import hudson.model.*;
import hudson.util.*;
import jenkins.model.*;
import hudson.FilePath.FileCallable;
import hudson.slaves.OfflineCause;
import hudson.node_monitors.*;

// small function to use for comparison of ws paths
def getTimeAccessed(ws) {
  pathStr = ws.getRemote()
  Path dir = Paths.get(pathStr) 
  BasicFileAttributes attrs = Files.readAttributes(dir, BasicFileAttributes)
  return attrs.lastAccessTime()  
}

def wsList = []

// Master
jInstance = Jenkins.instance
for (item in jInstance.items) {
  jobName = item.getFullDisplayName()
  
  if (item.isBuilding()) {
    println(".. job " + jobName + " is currently running, skipped")
    continue
  }
  
  workspacePath = jInstance.getWorkspaceFor(item)
  if (workspacePath == null) {
    println(".... could not get workspace path")
    continue
  }
  
  println(".... workspace = " + workspacePath)
  
  pathAsString = workspacePath.getRemote()
  if (workspacePath.exists()) {
    wsList << workspacePath
    println(".... added to list " + pathAsString)
  } else {
    println(".... nothing to delete at " + pathAsString)
  }
}

if (wsList) {
	wsList.sort{a,b -> getTimeAccessed(a)<=>getTimeAccessed(b)}
	for (ws in wsList[0..-3]) {
		println(".. proceeding to delete " + ws.getRemote())
		ws.deleteRecursive()
	}	
}

wsList = []

// Slaves 
for (node in jInstance.nodes) {
    computer = node.toComputer()
    if (computer.getChannel() == null) continue

    computer.setTemporarilyOffline(true, new hudson.slaves.OfflineCause.ByCLI("disk cleanup"))
    for (item in jInstance.items) {
      jobName = item.getFullDisplayName()
      
      if (item.isBuilding()) {
        println(".. job " + jobName + " is currently running, skipped")
        continue
      }
      
      workspacePath = node.getWorkspaceFor(item)
      if (workspacePath == null) {
        println(".... could not get workspace path")
        continue
      }
      
      println(".... workspace = " + workspacePath)

      pathAsString = workspacePath.getRemote()
      if (workspacePath.exists()) {
        wsList << workspacePath
        println(".... added " + pathAsString + " to list")
      } else {
        println(".... nothing to delete at " + pathAsString)
      }
    }

  computer.setTemporarilyOffline(false, null)
}

if (wsList) {
	wsList.sort{a,b -> getTimeAccessed(a)<=>getTimeAccessed(b)}
	for (ws in wsList[0..-3]) {
		println(".. proceeding to delete " + ws.getRemote())
		ws.deleteRecursive()
	}	
}

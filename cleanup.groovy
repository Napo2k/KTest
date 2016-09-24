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

def getTimeAccessed(path) {
  pathStr = path.getRemote()
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

wsList.sort{a,b -> getTimeAccessed(b)<=>getTimeAccessed(a)}
for (ws in wsList) {
  ws.deleteRecursive()
}

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
      println(".. wiping out workspaces of job " + jobName)
      
      workspacePath = node.getWorkspaceFor(item)
      if (workspacePath == null) {
        println(".... could not get workspace path")
        continue
      }
      
      println(".... workspace = " + workspacePath)

      pathAsString = workspacePath.getRemote()
      if (workspacePath.exists()) {
        workspacePath.deleteRecursive()
        println(".... deleted from location " + pathAsString)
      } else {
        println(".... nothing to delete at " + pathAsString)
      }
    }

  computer.setTemporarilyOffline(false, null)
}
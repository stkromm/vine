#!/bin/sh -e
cd D:/Workspaces/GameEngine
echo "Insert Commit message:"
read message
git add --all
git commit -m "$message"
git push origin
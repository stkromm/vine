#!/bin/sh -e
cd D:/Workspaces/GameEngine
echo "Insert Commit message:"
read 1
git add --all .
git commit -m '$1'
git push
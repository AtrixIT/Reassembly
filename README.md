Progect for Mysterria trial
----------------------------

Reassembly concept:
Its divided in two different implementation: Portal and Graft.

PORTALS
Portal will link two different places anywhere in the world.

Commands:
/portal getPortal: will give you the custom item that is able to create portals.
/portal list: will send a message in chat with all the portal u have created with their id and position.
/portal delete <id>: will delete the portal with the id specified (id's are from 0: the first portal created)

Info:
With left click you select the first position, with the right the second position to link to the first.
All portals are bidirectional.


GRAFT
Graft will change the structure of blocks and mobs, and its divided in 4 subimplementations:
1. BLOCK TO BLOCK: change a block into a preselected one.
2. MOB TO MOB: change a mob into a preselected one.
3. BLOCK TO MOB: change a block into a preselected mob.
4. MOB TO BLOCK: change a mob into a preselected block.

Commands:
/graft getGraft: will give you the custom item that is able to graft.
/graft: will open a gui where you can choose the 4 different options of graft

Info:
With left click while shifting you select the mob/block to replace blocks/mobs with.
With left click you replace blocks and mobs with the chosen block or mob,
    this only works if you preselected the mob/block with left click while shifting.
With right click you will swap through the graft modalities.

All custom items dropped will disappear and you will have to reuse the command to reacquire them

I did use Revxrsal/Lamp and InvUI libraries.
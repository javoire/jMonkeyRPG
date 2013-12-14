# Notes to self

## Blender

Set shading to "smooth" in edit mode to get smooth shading in jmonkey
! Sometimes setting shading to smooth will make the loadAsset(blenderfilepath..) to just freeze when launching the game.... 
!! It doesn't feeze, just takes ages to load .blend file

When adding new assets OR updating existing ones, don't forget to refresh folders in eclipse! (otherwise AssetNotFoundException or new changes won't show)

When parenting many trees to one empty object, they bug in the game, only visible from certain angles etc...

Paint RGB on terrainmesh, save the alphamap.png and use as alphamap in jmonkey material

Always unwrap objects to give them a UV map. jMonkey needs them to have a UV map when applying a j3m material.

If 3D texture paint isnt working:
1. Go to your 3D view panel, hit TAB.
2. Now with your 3D view in Edit Mode, your UV editing panel (on the left side) should show the UV lines.
3. Select the image texture that you like to Texture Paint in the UV Editing panel.
4. Hit TAB again in 3D view. Now painting in 3D view is working

if assetManger.loadModel("path") and rootNode.attachChild(model) puts invisible models in game, it's LIKELY because the material isn't working => invisible geometry
- NO! it's because there's no light INSIDE the j3o object file.. which is wrong obv => the lightsource of the main terrain scene doesn't affect this new object... ! fix: get the lights from the scene model (lightlist), add them to rootNode instead (then clear scene.lightList)

if the jmonkey IDE freezes on launch.. kill it and wait a minute before relaunching

## SceneComposer 

If an object (for some reason) is distanced from its origin, save and reopen the scene
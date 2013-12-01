# jMonkeyEngine 3 test game

## ERROR FIXES:

* when complaining about jar version mismatch native regarding lwjgl => delete liblwjgl.jnilib and restart JMP

* If textures go black in blender, try:
** flipping normals in edit mode or
** checking/unchecking premultiply on every texture

## OLD Notes to self:

* using heightmaps for terrain
** create in blender with blend-texture
** create rgb-alphamap in blender
** render with orthogonal 512x512 camera in top view

* alphatextures:
** use png with alphachannel to make it work with shadowrenderer

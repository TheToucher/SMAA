#define SMAA_INCLUDE_PS 1
#import "MatDefs/Post/TTTR/SMAA/SMAALib.glsllib"

out vec4 outColor;

in vec2 texCoord;
in vec4[3] offset;
in vec2 pixcoord;
uniform sampler2D m_AreaTexture;
uniform sampler2D m_SearchTexture;
uniform sampler2D m_EdgesTexture;


void main() {

outColor =  SMAABlendingWeightCalculationPS(texCoord,
                                       pixcoord,
                                       offset,
                                       m_EdgesTexture,
                                       m_AreaTexture,
                                       m_SearchTexture,
                                       vec4(0.0)); // Just pass zero for SMAA 1x, see @SUBSAMPLE_INDICES.



}
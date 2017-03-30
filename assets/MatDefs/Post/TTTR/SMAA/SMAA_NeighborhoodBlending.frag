#define SMAA_INCLUDE_PS 1
#import "MatDefs/Post/TTTR/SMAA/SMAALib.glsllib"

out vec4 outColor;
in vec2 texCoord;
uniform sampler2D m_Texture;
uniform sampler2D m_WeightsTexture;
in vec4 offset2;

void main() {

outColor = SMAANeighborhoodBlendingPS(texCoord,
                                  offset2,
                                  m_Texture,
                                  m_WeightsTexture
);
}



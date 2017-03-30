#define SMAA_INCLUDE_PS 1
#import "MatDefs/Post/TTTR/SMAA/SMAALib.glsllib"

out vec4 outColor;
in vec2 texCoord;
in vec2 texcoord;
uniform sampler2D m_Texture;
uniform sampler2D m_DepthTexture;
in vec4[3] offset;

void main() {
    outColor = vec4(


SMAADepthEdgeDetectionPS(texcoord,
                                offset,
                                m_DepthTexture
        ), 0.0,1.0);
}

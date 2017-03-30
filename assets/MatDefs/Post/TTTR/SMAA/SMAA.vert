#define SMAA_INCLUDE_VS 1
#define SMAA_INCLUDE_PS 0
#import "MatDefs/Post/TTTR/SMAA/SMAALib.glsllib"


out vec4[3] offset;

attribute vec3 inPosition;
attribute vec2 inTexCoord;




#ifdef VS_BLENDING
//-----------------------------------------------------------------------------
/**
 * Blend Weight Calculation Vertex Shader
 */

out vec2 pixCoord;
out vec2 pixcoord;

#endif


#ifdef VS_NEIGHBORHOODBLENDING
//-----------------------------------------------------------------------------
/**
 * Neighborhood Blending Vertex Shader
 */

out vec4 offset2;

void SMAANeighborhoodBlendingVS(vec2 texcoord) {
    offset2 = mad(SMAA_RT_METRICS.xyxy, vec4( 1.0, 0.0, 0.0,  1.0), texcoord.xyxy);
}
    
#endif


void SMAABlendingWeightCalculationVS(float2 texcoord,
                                     out float4 offset[3]) {

    // We will use these offsets for the searches later on (see @PSEUDO_GATHER4):
    offset[0] = mad(SMAA_RT_METRICS.xyxy, float4(-0.25, -0.125,  1.25, -0.125), texcoord.xyxy);
    offset[1] = mad(SMAA_RT_METRICS.xyxy, float4(-0.125, -0.25, -0.125,  1.25), texcoord.xyxy);

    // And these for the searches, they indicate the ends of the loops:
    offset[2] = mad(SMAA_RT_METRICS.xxyy,
                    float4(-2.0, 2.0, -2.0, 2.0) * float(SMAA_MAX_SEARCH_STEPS),
                    float4(offset[0].xz, offset[1].yw));
}






varying vec2 texCoord;
varying vec2 texcoord;

void main() {

    texCoord = inTexCoord;
    texcoord = inTexCoord;

    vec4 offsets[3];
    offsets[0] = vec4(0.0, 0.0, 0.0, 0.0);
    offsets[1] = vec4(0.0, 0.0, 0.0, 0.0);
    offsets[2] = vec4(0.0, 0.0, 0.0, 0.0);

    #ifdef VS_EDGEDETECTION
        //SMAAEdgeDetectionVS(texcoord);

        SMAAEdgeDetectionVS(texcoord, offsets);

    #endif
    #ifdef VS_BLENDING
        //SMAABlendingWeightCalculationVS(texCoord);
        vec2 pixCoord = vec2(0.0);
        SMAABlendingWeightCalculationVS(texCoord,pixCoord,offsets);
        pixcoord = pixCoord;//*vec2(1.0,1.0)-vec2(0.0,0.0);
    #endif
    
    #ifdef VS_NEIGHBORHOODBLENDING
        SMAANeighborhoodBlendingVS(texCoord);
    #endif

    offset = offsets;
    
    vec2 pos = inPosition.xy * 2.0 - 1.0;
    gl_Position = vec4(pos, 0.0, 1.0);    
}
uniform mat4 g_WorldViewProjectionMatrix;
uniform vec4 g_LightPosition;
attribute vec3 inPosition;
attribute vec3 inNormal;

varying float intensity;

#if defined(HAS_COLORMAP) || (defined(HAS_LIGHTMAP) && !defined(SEPARATE_TEXCOORD))
    #define NEED_TEXCOORD1
#endif

#ifdef NEED_TEXCOORD1
    attribute vec3 inTexCoord;
    varying vec3 texCoord1;
#endif

#ifdef SEPARATE_TEXCOORD
    attribute vec3 inTexCoord2;
    varying vec3 texCoord2;
#endif

#ifdef HAS_VERTEXCOLOR
    attribute vec4 inColor;
    varying vec4 vertColor;
#endif

void main(){

    vec3 lightDirection = vec3(g_LightPosition[0], g_LightPosition[1], g_LightPosition[2]);
    intensity = dot(lightDirection, gl_Normal);

    #ifdef NEED_TEXCOORD1
        texCoord1 = inTexCoord;
    #endif

    #ifdef SEPARATE_TEXCOORD
        texCoord2 = inTexCoord2;
    #endif

    #ifdef HAS_VERTEXCOLOR
        vertColor = inColor;
    #endif

    gl_Position = g_WorldViewProjectionMatrix * vec4(inPosition, 1.0);
}
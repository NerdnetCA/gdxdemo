
varying vec4 v_color;
varying vec2 v_texCoord0;

uniform sampler2D u_texture;
uniform float brite;
uniform float gray;

/* Value/Saturation shader */

/* rgb to hsv tool. Not mine */
vec3 rgb2hsv(vec3 c)
{
    vec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);
    vec4 p = mix(vec4(c.bg, K.wz), vec4(c.gb, K.xy), step(c.b, c.g));
    vec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));

    float d = q.x - min(q.w, q.y);
    float e = 1.0e-10;
    return vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);
}
/* hsv to rgb tool. Not mine */
vec3 hsv2rgb(vec3 c)
{
    vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
    vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
}

/* Basic value and saturation modifier.
 Note: as with many of my shaders, I'm throwing away the alpha
 channel. This is usually fine, since I apply these to a framebuffer,
 but this should be kept in mind when adapting this shader to other uses.
 */
void main() {
    vec2 tc = v_texCoord0;
    vec3 col = texture2D(u_texture, tc).rgb;
    vec3 hsv = rgb2hsv(col);
    hsv.y *= gray;
    hsv.z *= brite;
    col = hsv2rgb(hsv);
    gl_FragColor = vec4(col,1.0);
}

#version 330

//in vec3 GStoFSColor[];
in GS_OUT{
    vec3 color;
    vec3 modelViewVertexNormal;
    vec3 modelViewVertexposition;
} fs_in[1];
out vec4 fragColor;

struct Attenuation
{
    float constant;
    float linear;
    float exponent;
};

struct PointLight
{
    vec3 colour;
// Light position is assumed to be in view coordinates
    vec3 position;
    float intensity;
    Attenuation att;
};

struct Material
{
    vec4 ambient;
    vec4 diffuse;
    vec4 specular;
    int hasTexture;
    float reflectance;
};

uniform vec3 ambientLight;
uniform float specularPower;
uniform Material material;
uniform PointLight pointLight;

vec4 ambientC;
vec4 diffuseC;
vec4 speculrC;

void setupColours(Material material)
{
    ambientC = material.ambient;
    diffuseC = material.diffuse;
    speculrC = material.specular;
//    ambientC = vec4(fs_in[0].color,1.0);
//    diffuseC = vec4(fs_in[0].color,1.0);
//    speculrC = vec4(fs_in[0].color,1.0);
}


vec4 calcPointLight(PointLight light, vec3 position, vec3 normal)
{
    vec4 diffuseColour = vec4(0, 0, 0, 0);
    vec4 specColour = vec4(0, 0, 0, 0);

    // Diffuse Light
    vec3 light_direction = light.position - position;
    vec3 to_light_source  = normalize(light_direction);
    float diffuseFactor = max(dot(normal, to_light_source ), 0.0);
    diffuseColour = diffuseC * vec4(light.colour, 1.0) * light.intensity * diffuseFactor;

    // Specular Light
    vec3 camera_direction = normalize(-position);
    vec3 from_light_source = -to_light_source;
    vec3 reflected_light = normalize(reflect(from_light_source, normal));
    float specularFactor = max( dot(camera_direction, reflected_light), 0.0);
    specularFactor = pow(specularFactor, specularPower);
    specColour = speculrC * specularFactor * material.reflectance * vec4(light.colour, 1.0);

    // Attenuation
    float distance = length(light_direction);
    float attenuationInv = light.att.constant + light.att.linear * distance +
    light.att.exponent * distance * distance;
    return (diffuseColour + specColour) / attenuationInv;
}

void main()
{
    setupColours(material);

    vec4 diffuseSpecularComp = calcPointLight(pointLight, fs_in[0].modelViewVertexposition, fs_in[0].modelViewVertexNormal);

    fragColor = ambientC * vec4(ambientLight, 1) + diffuseSpecularComp;
}
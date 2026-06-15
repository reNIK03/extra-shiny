package net.r_nik.extrashiny.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import org.joml.Vector3f;

public class AuroralDustColorTransitionOptions implements ParticleOptions {

    public static final Codec<AuroralDustColorTransitionOptions> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.FLOAT.fieldOf("from_r").forGetter(o -> o.fromColor.x),
            Codec.FLOAT.fieldOf("from_g").forGetter(o -> o.fromColor.y),
            Codec.FLOAT.fieldOf("from_b").forGetter(o -> o.fromColor.z),
            Codec.FLOAT.fieldOf("to_r").forGetter(o -> o.toColor.x),
            Codec.FLOAT.fieldOf("to_g").forGetter(o -> o.toColor.y),
            Codec.FLOAT.fieldOf("to_b").forGetter(o -> o.toColor.z),
            Codec.FLOAT.fieldOf("scale").forGetter(o -> o.scale)
    ).apply(inst, (fr, fg, fb, tr, tg, tb, s) ->
            new AuroralDustColorTransitionOptions(new Vector3f(fr, fg, fb), new Vector3f(tr, tg, tb), s)
    ));

    public static final Deserializer<AuroralDustColorTransitionOptions> DESERIALIZER =
            new Deserializer<>() {
                @Override
                public AuroralDustColorTransitionOptions fromCommand(ParticleType<AuroralDustColorTransitionOptions> type, StringReader reader)
                        throws CommandSyntaxException {

                    reader.expect(' ');
                    float fr = reader.readFloat(); reader.expect(' ');
                    float fg = reader.readFloat(); reader.expect(' ');
                    float fb = reader.readFloat(); reader.expect(' ');
                    float tr = reader.readFloat(); reader.expect(' ');
                    float tg = reader.readFloat(); reader.expect(' ');
                    float tb = reader.readFloat(); reader.expect(' ');
                    float scale = reader.readFloat();

                    return new AuroralDustColorTransitionOptions(
                            new Vector3f(fr, fg, fb),
                            new Vector3f(tr, tg, tb),
                            scale
                    );
                }

                @Override
                public AuroralDustColorTransitionOptions fromNetwork(ParticleType<AuroralDustColorTransitionOptions> type, FriendlyByteBuf buf) {
                    float fr = buf.readFloat();
                    float fg = buf.readFloat();
                    float fb = buf.readFloat();
                    float tr = buf.readFloat();
                    float tg = buf.readFloat();
                    float tb = buf.readFloat();
                    float scale = buf.readFloat();

                    return new AuroralDustColorTransitionOptions(
                            new Vector3f(fr, fg, fb),
                            new Vector3f(tr, tg, tb),
                            scale
                    );
                }
            };

    private final Vector3f fromColor;
    private final Vector3f toColor;
    private final float scale;

    public AuroralDustColorTransitionOptions(Vector3f fromColor, Vector3f toColor, float scale) {
        this.fromColor = new Vector3f(Mth.clamp(fromColor.x, 0f, 1f), Mth.clamp(fromColor.y, 0f, 1f), Mth.clamp(fromColor.z, 0f, 1f));
        this.toColor   = new Vector3f(Mth.clamp(toColor.x, 0f, 1f), Mth.clamp(toColor.y, 0f, 1f), Mth.clamp(toColor.z, 0f, 1f));
        this.scale = Mth.clamp(scale, 0.01f, 4.0f);
    }

    @Override
    public ParticleType<?> getType() {
        return ModParticleTypes.AURORAL_DUST.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buf) {
        buf.writeFloat(fromColor.x);
        buf.writeFloat(fromColor.y);
        buf.writeFloat(fromColor.z);
        buf.writeFloat(toColor.x);
        buf.writeFloat(toColor.y);
        buf.writeFloat(toColor.z);
        buf.writeFloat(scale);
    }

    @Override
    public String writeToString() {
        return String.format(java.util.Locale.ROOT,
                "%f %f %f %f %f %f %f",
                fromColor.x, fromColor.y, fromColor.z,
                toColor.x, toColor.y, toColor.z,
                scale
        );
    }

    public Vector3f getFromColor() { return fromColor; }
    public Vector3f getToColor() { return toColor; }
    public float getScale() { return scale; }
}

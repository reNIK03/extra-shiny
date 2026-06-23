package net.r_nik.extrashiny.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;
import org.joml.Vector3f;

public class AuroralDustColorTransitionOptions implements ParticleOptions {

    public static final MapCodec<AuroralDustColorTransitionOptions> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
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

    public static final StreamCodec<ByteBuf, AuroralDustColorTransitionOptions> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VECTOR3F, o -> o.fromColor,
            ByteBufCodecs.VECTOR3F, o -> o.toColor,
            ByteBufCodecs.FLOAT, o -> o.scale,
            AuroralDustColorTransitionOptions::new
    );

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

    public Vector3f getFromColor() { return fromColor; }
    public Vector3f getToColor() { return toColor; }
    public float getScale() { return scale; }
}
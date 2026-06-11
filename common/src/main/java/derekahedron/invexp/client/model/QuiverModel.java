package derekahedron.invexp.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;

/**
 * Defines a model for a quiver which can be rendered on a player.
 */
public class QuiverModel extends Model {

    public final ModelPart root;

    /**
     * Creates a new quiver model.
     *
     * @param root the root model part to build on
     */
    public QuiverModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.root = root;
    }

    @Override
    public void renderToBuffer(
            PoseStack matrixStack,
            VertexConsumer vertexConsumer,
            int light,
            int overlay,
            float red,
            float green,
            float blue,
            float alpha) {
        root.render(
                matrixStack,
                vertexConsumer,
                light,
                overlay,
                red,
                green,
                blue,
                alpha);
    }

    /**
     * Creates a layer definition for the quiver model.
     *
     * @return the layer definition of a quiver model
     */
    public static LayerDefinition createQuiverLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition root = meshDefinition.getRoot();

        root.addOrReplaceChild(
                "quiver",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(
                                -2.0F, -6.0F, 0.0F,
                                4.0F, 12.0F, 3.0F)
                        .texOffs(16, 0)
                        .addBox(
                                -2.0F, -6.0F, 0.0F,
                                4.0F, 12.0F, 3.0F,
                                new CubeDeformation(0.1F)),
                PartPose.ZERO);

        return LayerDefinition.create(meshDefinition, 32, 16);
    }
}

package pm.meh.ioticspellbooks.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import pm.meh.ioticspellbooks.IoticSpellbooks;

public class ConjuredSpellbookModel<T extends ConjuredSpellbookEntity> extends HierarchicalModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(IoticSpellbooks.MODID, "conjured_book"), "main");
	private final ModelPart book;

    public ConjuredSpellbookModel(ModelPart root) {
		this.book = root.getChild("book");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition book = partdefinition.addOrReplaceChild("book", CubeListBuilder.create().texOffs(18, 26).addBox(-1.0F, -16.0F, 5.0F, 2.0F, 16.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		book.addOrReplaceChild("left_cover", CubeListBuilder.create().texOffs(0, 0).addBox(0.0001F, -16.0F, -10.0F, 1.0F, 16.0F, 10.0F, new CubeDeformation(-0.0001F)), PartPose.offset(0.0F, 0.0F, 5.0F));
		book.addOrReplaceChild("page", CubeListBuilder.create().texOffs(0, 26).addBox(0.0F, -15.0F, -9.0F, 0.0F, 14.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 5.0F));
		book.addOrReplaceChild("right_cover", CubeListBuilder.create().texOffs(22, 0).addBox(-1.0001F, -16.0F, -10.0F, 1.0F, 16.0F, 10.0F, new CubeDeformation(-0.0001F)), PartPose.offset(0.0F, 0.0F, 5.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(@NotNull ConjuredSpellbookEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.animate(entity.openAnimationState, ConjuredSpellbookAnimation.OPEN, ageInTicks, 1);
        this.animate(entity.closeAnimationState, ConjuredSpellbookAnimation.CLOSE, ageInTicks, 1);
        this.animate(entity.castAnimationState, ConjuredSpellbookAnimation.CAST, ageInTicks, 1);
	}

	@Override
	public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		book.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

    @Override
    public ModelPart root() {
        return book;
    }
}
package elem.ui;

import org.lwjgl.nuklear.NkImage;

import engine.graphics.Texture;

public class UINkImage {

	public static NkImage createNkImage(String imagepath) {
		Texture text = new Texture("/pics/" + imagepath);
		
		NkImage image = NkImage.create();
		image.handle(it -> it.id(text.getTextureID()));
		
		return image;
	}

}

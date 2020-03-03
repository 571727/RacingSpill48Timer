package engine.io;

import static org.lwjgl.nuklear.Nuklear.*;
import static org.lwjgl.opengl.GL11C.GL_LINEAR;
import static org.lwjgl.opengl.GL11C.GL_RGBA;
import static org.lwjgl.opengl.GL11C.GL_RGBA8;
import static org.lwjgl.opengl.GL11C.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11C.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11C.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11C.glBindTexture;
import static org.lwjgl.opengl.GL11C.glGenTextures;
import static org.lwjgl.opengl.GL11C.glTexImage2D;
import static org.lwjgl.opengl.GL11C.glTexParameteri;
import static org.lwjgl.opengl.GL12C.GL_UNSIGNED_INT_8_8_8_8_REV;
import static org.lwjgl.stb.STBTruetype.stbtt_GetCodepointHMetrics;
import static org.lwjgl.stb.STBTruetype.stbtt_GetFontVMetrics;
import static org.lwjgl.stb.STBTruetype.stbtt_GetPackedQuad;
import static org.lwjgl.stb.STBTruetype.stbtt_InitFont;
import static org.lwjgl.stb.STBTruetype.stbtt_PackBegin;
import static org.lwjgl.stb.STBTruetype.stbtt_PackEnd;
import static org.lwjgl.stb.STBTruetype.stbtt_PackFontRange;
import static org.lwjgl.stb.STBTruetype.stbtt_PackSetOversampling;
import static org.lwjgl.stb.STBTruetype.stbtt_ScaleForPixelHeight;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryUtil.memAddress;
import static org.lwjgl.system.MemoryUtil.memAlloc;
import static org.lwjgl.system.MemoryUtil.memFree;
import static org.lwjgl.system.MemoryUtil.nmemAllocChecked;
import static org.lwjgl.system.MemoryUtil.nmemFree;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.nuklear.NkAllocator;
import org.lwjgl.nuklear.NkBuffer;
import org.lwjgl.nuklear.NkContext;
import org.lwjgl.nuklear.NkDrawNullTexture;
import org.lwjgl.nuklear.NkDrawVertexLayoutElement;
import org.lwjgl.nuklear.NkUserFont;
import org.lwjgl.nuklear.NkUserFontGlyph;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTTPackContext;
import org.lwjgl.stb.STBTTPackedchar;
import org.lwjgl.system.MemoryStack;

import engine.graphics.UIRender;
import engine.utils.FileUtils;

public class UI {

	public static final int BUFFER_INITIAL_SIZE = 4 * 1024;
	public static final NkDrawVertexLayoutElement.Buffer VERTEX_LAYOUT;
	public static final int MAX_VERTEX_BUFFER  = 512 * 1024;
	public static final int MAX_ELEMENT_BUFFER = 128 * 1024;
	public static final NkAllocator ALLOCATOR;

	static {
		ALLOCATOR = NkAllocator.create()
				.alloc((handle, old, size) -> nmemAllocChecked(size)).mfree((handle, ptr) -> nmemFree(ptr));
		
		VERTEX_LAYOUT = NkDrawVertexLayoutElement.create(4)
				.position(0).attribute(NK_VERTEX_POSITION).format(NK_FORMAT_FLOAT).offset(0)
				.position(1).attribute(NK_VERTEX_TEXCOORD).format(NK_FORMAT_FLOAT).offset(8)
				.position(2).attribute(NK_VERTEX_COLOR).format(NK_FORMAT_R8G8B8A8).offset(16)
				.position(3).attribute(NK_VERTEX_ATTRIBUTE_COUNT).format(NK_FORMAT_COUNT).offset(0)
				.flip();
	}
	
	
	private NkContext ctx = NkContext.create();
	private NkUserFont default_font = NkUserFont.create();
	private final ByteBuffer ttf;
	
	
	public UI() {
		try {
            this.ttf = FileUtils.ioResourceToByteBuffer("resources/fonts/BASKVILL.TTF", 512 * 1024);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
	}
	
	public UIRender setupContext() {
		return new UIRender();
	}
	

	public void nkFont() {
		int BITMAP_W = 1024;
		int BITMAP_H = 1024;

		int FONT_HEIGHT = Window.CURRENT_HEIGHT / 38;
		int fontTexID   = glGenTextures();

		STBTTFontinfo          fontInfo = STBTTFontinfo.create();
		STBTTPackedchar.Buffer cdata    = STBTTPackedchar.create(95);

		float scale;
		float descent;

		try (MemoryStack stack = stackPush()) {
		    stbtt_InitFont(fontInfo, ttf);
		    scale = stbtt_ScaleForPixelHeight(fontInfo, FONT_HEIGHT);

		    IntBuffer d = stack.mallocInt(1);
		    stbtt_GetFontVMetrics(fontInfo, null, d, null);
		    descent = d.get(0) * scale;

		    ByteBuffer bitmap = memAlloc(BITMAP_W * BITMAP_H);

		    STBTTPackContext pc = STBTTPackContext.mallocStack(stack);
		    stbtt_PackBegin(pc, bitmap, BITMAP_W, BITMAP_H, 0, 1, NULL);
		    stbtt_PackSetOversampling(pc, 4, 4);
		    stbtt_PackFontRange(pc, ttf, 0, FONT_HEIGHT, 32, cdata);
		    stbtt_PackEnd(pc);

		    // Convert R8 to RGBA8
		    ByteBuffer texture = memAlloc(BITMAP_W * BITMAP_H * 4);
		    for (int i = 0; i < bitmap.capacity(); i++) {
		        texture.putInt((bitmap.get(i) << 24) | 0x00FFFFFF);
		    }
		    texture.flip();

		    glBindTexture(GL_TEXTURE_2D, fontTexID);
		    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, BITMAP_W, BITMAP_H, 0, GL_RGBA, GL_UNSIGNED_INT_8_8_8_8_REV, texture);
		    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

		    memFree(texture);
		    memFree(bitmap);
		}

		default_font
		    .width((handle, h, text, len) -> {
		        float text_width = 0;
		        try (MemoryStack stack = stackPush()) {
		            IntBuffer unicode = stack.mallocInt(1);

		            int glyph_len = nnk_utf_decode(text, memAddress(unicode), len);
		            int text_len  = glyph_len;

		            if (glyph_len == 0) {
		                return 0;
		            }

		            IntBuffer advance = stack.mallocInt(1);
		            while (text_len <= len && glyph_len != 0) {
		                if (unicode.get(0) == NK_UTF_INVALID) {
		                    break;
		                }

		                /* query currently drawn glyph information */
		                stbtt_GetCodepointHMetrics(fontInfo, unicode.get(0), advance, null);
		                text_width += advance.get(0) * scale;

		                /* offset next glyph */
		                glyph_len = nnk_utf_decode(text + text_len, memAddress(unicode), len - text_len);
		                text_len += glyph_len;
		            }
		        }
		        return text_width;
		    })
		    .height(FONT_HEIGHT)
		    .query((handle, font_height, glyph, codepoint, next_codepoint) -> {
		        try (MemoryStack stack = stackPush()) {
		            FloatBuffer x = stack.floats(0.0f);
		            FloatBuffer y = stack.floats(0.0f);

		            STBTTAlignedQuad q       = STBTTAlignedQuad.mallocStack(stack);
		            IntBuffer        advance = stack.mallocInt(1);

		            stbtt_GetPackedQuad(cdata, BITMAP_W, BITMAP_H, codepoint - 32, x, y, q, false);
		            stbtt_GetCodepointHMetrics(fontInfo, codepoint, advance, null);

		            NkUserFontGlyph ufg = NkUserFontGlyph.create(glyph);

		            ufg.width(q.x1() - q.x0());
		            ufg.height(q.y1() - q.y0());
		            ufg.offset().set(q.x0(), q.y0() + (FONT_HEIGHT + descent));
		            ufg.xadvance(advance.get(0) * scale);
		            ufg.uv(0).set(q.s0(), q.t0());
		            ufg.uv(1).set(q.s1(), q.t1());
		        }
		    })
		    .texture(it -> it
		        .id(fontTexID));

		nk_style_set_font(ctx, default_font);
	}

	public NkContext getNkContext() {
		return ctx;
	}
	
}

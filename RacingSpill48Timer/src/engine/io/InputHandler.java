package engine.io;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.nuklear.Nuklear.NK_BUTTON_LEFT;
import static org.lwjgl.nuklear.Nuklear.NK_BUTTON_MIDDLE;
import static org.lwjgl.nuklear.Nuklear.NK_BUTTON_RIGHT;
import static org.lwjgl.nuklear.Nuklear.NK_KEY_BACKSPACE;
import static org.lwjgl.nuklear.Nuklear.NK_KEY_COPY;
import static org.lwjgl.nuklear.Nuklear.NK_KEY_CUT;
import static org.lwjgl.nuklear.Nuklear.NK_KEY_DEL;
import static org.lwjgl.nuklear.Nuklear.NK_KEY_DOWN;
import static org.lwjgl.nuklear.Nuklear.NK_KEY_ENTER;
import static org.lwjgl.nuklear.Nuklear.NK_KEY_LEFT;
import static org.lwjgl.nuklear.Nuklear.NK_KEY_PASTE;
import static org.lwjgl.nuklear.Nuklear.NK_KEY_RIGHT;
import static org.lwjgl.nuklear.Nuklear.NK_KEY_SCROLL_DOWN;
import static org.lwjgl.nuklear.Nuklear.NK_KEY_SCROLL_END;
import static org.lwjgl.nuklear.Nuklear.NK_KEY_SCROLL_START;
import static org.lwjgl.nuklear.Nuklear.NK_KEY_SCROLL_UP;
import static org.lwjgl.nuklear.Nuklear.NK_KEY_SHIFT;
import static org.lwjgl.nuklear.Nuklear.NK_KEY_TAB;
import static org.lwjgl.nuklear.Nuklear.NK_KEY_TEXT_END;
import static org.lwjgl.nuklear.Nuklear.NK_KEY_TEXT_LINE_END;
import static org.lwjgl.nuklear.Nuklear.NK_KEY_TEXT_LINE_START;
import static org.lwjgl.nuklear.Nuklear.NK_KEY_TEXT_REDO;
import static org.lwjgl.nuklear.Nuklear.NK_KEY_TEXT_START;
import static org.lwjgl.nuklear.Nuklear.NK_KEY_TEXT_UNDO;
import static org.lwjgl.nuklear.Nuklear.NK_KEY_TEXT_WORD_LEFT;
import static org.lwjgl.nuklear.Nuklear.NK_KEY_TEXT_WORD_RIGHT;
import static org.lwjgl.nuklear.Nuklear.NK_KEY_UP;
import static org.lwjgl.nuklear.Nuklear.nk_init;
import static org.lwjgl.nuklear.Nuklear.nk_input_button;
import static org.lwjgl.nuklear.Nuklear.nk_input_key;
import static org.lwjgl.nuklear.Nuklear.nk_input_motion;
import static org.lwjgl.nuklear.Nuklear.nk_input_scroll;
import static org.lwjgl.nuklear.Nuklear.nk_input_unicode;
import static org.lwjgl.nuklear.Nuklear.nnk_strlen;
import static org.lwjgl.nuklear.Nuklear.nnk_textedit_paste;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryUtil.memAddress;
import static org.lwjgl.system.MemoryUtil.memCopy;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import static org.lwjgl.glfw.Callbacks.*;
import org.lwjgl.glfw.*;
import org.lwjgl.nuklear.NkContext;
import org.lwjgl.nuklear.NkVec2;
import org.lwjgl.system.MemoryStack;

import file_manipulation.ControlsSettings;
import scenes.Scene;

public class InputHandler {

	private double x, y;
	private Scene currentScene;
	private ControlsSettings keys;

	public InputHandler(Window win, NkContext ctx) {
		keys = new ControlsSettings();

		long myWindow = win.getWindow();

		glfwSetCursorEnterCallback(myWindow, GLFWCursorEnterCallback.create((window, entered) -> {
			this.currentScene.mouseEnterWindowInput(entered);
		}));

		glfwSetKeyCallback(myWindow, GLFWKeyCallback.create((window, key, scancode, action, mods) -> {
			if (this.currentScene.keyInput(key, action)) {
				boolean press = action == GLFW_PRESS;
				switch (key) {
				
				case GLFW_KEY_DELETE:
					nk_input_key(ctx, NK_KEY_DEL, press);
					break;
				case GLFW_KEY_ENTER:
					nk_input_key(ctx, NK_KEY_ENTER, press);
					break;
				case GLFW_KEY_TAB:
					nk_input_key(ctx, NK_KEY_TAB, press);
					break;
				case GLFW_KEY_BACKSPACE:
					nk_input_key(ctx, NK_KEY_BACKSPACE, press);
					break;
				case GLFW_KEY_UP:
					nk_input_key(ctx, NK_KEY_UP, press);
					break;
				case GLFW_KEY_DOWN:
					nk_input_key(ctx, NK_KEY_DOWN, press);
					break;
				case GLFW_KEY_HOME:
					nk_input_key(ctx, NK_KEY_TEXT_START, press);
					nk_input_key(ctx, NK_KEY_SCROLL_START, press);
					break;
				case GLFW_KEY_END:
					nk_input_key(ctx, NK_KEY_TEXT_END, press);
					nk_input_key(ctx, NK_KEY_SCROLL_END, press);
					break;
				case GLFW_KEY_PAGE_DOWN:
					nk_input_key(ctx, NK_KEY_SCROLL_DOWN, press);
					break;
				case GLFW_KEY_PAGE_UP:
					nk_input_key(ctx, NK_KEY_SCROLL_UP, press);
					break;
				case GLFW_KEY_LEFT_SHIFT:
				case GLFW_KEY_RIGHT_SHIFT:
					nk_input_key(ctx, NK_KEY_SHIFT, press);
					break;
				case GLFW_KEY_LEFT_CONTROL:
				case GLFW_KEY_RIGHT_CONTROL:
					if (press) {
						nk_input_key(ctx, NK_KEY_COPY, glfwGetKey(window, GLFW_KEY_C) == GLFW_PRESS);
						nk_input_key(ctx, NK_KEY_PASTE, glfwGetKey(window, GLFW_KEY_P) == GLFW_PRESS);
						nk_input_key(ctx, NK_KEY_CUT, glfwGetKey(window, GLFW_KEY_X) == GLFW_PRESS);
						nk_input_key(ctx, NK_KEY_TEXT_UNDO, glfwGetKey(window, GLFW_KEY_Z) == GLFW_PRESS);
						nk_input_key(ctx, NK_KEY_TEXT_REDO, glfwGetKey(window, GLFW_KEY_Y) == GLFW_PRESS);
						nk_input_key(ctx, NK_KEY_TEXT_WORD_LEFT, glfwGetKey(window, GLFW_KEY_LEFT) == GLFW_PRESS);
						nk_input_key(ctx, NK_KEY_TEXT_WORD_RIGHT, glfwGetKey(window, GLFW_KEY_RIGHT) == GLFW_PRESS);
						nk_input_key(ctx, NK_KEY_TEXT_LINE_START, glfwGetKey(window, GLFW_KEY_B) == GLFW_PRESS);
						nk_input_key(ctx, NK_KEY_TEXT_LINE_END, glfwGetKey(window, GLFW_KEY_E) == GLFW_PRESS);
					} else {
						nk_input_key(ctx, NK_KEY_LEFT, glfwGetKey(window, GLFW_KEY_LEFT) == GLFW_PRESS);
						nk_input_key(ctx, NK_KEY_RIGHT, glfwGetKey(window, GLFW_KEY_RIGHT) == GLFW_PRESS);
						nk_input_key(ctx, NK_KEY_COPY, false);
						nk_input_key(ctx, NK_KEY_PASTE, false);
						nk_input_key(ctx, NK_KEY_CUT, false);
						nk_input_key(ctx, NK_KEY_SHIFT, false);
					}
					break;
				}
			}
		}));

		glfwSetMouseButtonCallback(myWindow, GLFWMouseButtonCallback.create((window, button, action, mods) -> {
			try (MemoryStack stack = stackPush()) {
				DoubleBuffer cx = stack.mallocDouble(1);
				DoubleBuffer cy = stack.mallocDouble(1);

				glfwGetCursorPos(window, cx, cy);

				int x = (int) cx.get(0);
				int y = (int) cy.get(0);

				int nkButton;
				switch (button) {
				case GLFW_MOUSE_BUTTON_RIGHT:
					nkButton = NK_BUTTON_RIGHT;
					break;
				case GLFW_MOUSE_BUTTON_MIDDLE:
					nkButton = NK_BUTTON_MIDDLE;
					break;
				default:
					nkButton = NK_BUTTON_LEFT;
				}
				nk_input_button(ctx, nkButton, x, y, action == GLFW_PRESS);
			}
			this.currentScene.mouseButtonInput(button, action, x, y);
		}));

		glfwSetCursorPosCallback(myWindow, GLFWCursorPosCallback.create((window, xpos, ypos) -> {

			nk_input_motion(ctx, (int) xpos, (int) ypos);

			x = xpos;
			y = ypos;
			this.currentScene.mousePosInput(xpos, ypos);
		}));

		glfwSetScrollCallback(myWindow, GLFWScrollCallback.create((window, xoffset, yoffset) -> {
			try (MemoryStack stack = stackPush()) {
				NkVec2 scroll = NkVec2.mallocStack(stack).x((float) xoffset).y((float) yoffset);
				nk_input_scroll(ctx, scroll);
			}
			this.currentScene.mouseScrollInput(xoffset, yoffset);
		}));

		glfwSetCharCallback(myWindow, (window, codepoint) -> nk_input_unicode(ctx, codepoint));

		nk_init(ctx, UI.ALLOCATOR, null);
		ctx.clip().copy((handle, text, len) -> {
			if (len == 0) {
				return;
			}

			try (MemoryStack stack = stackPush()) {
				ByteBuffer str = stack.malloc(len + 1);
				memCopy(text, memAddress(str), len);
				str.put(len, (byte) 0);

				glfwSetClipboardString(myWindow, str);
			}
		}).paste((handle, edit) -> {
			long text = nglfwGetClipboardString(myWindow);
			if (text != NULL) {
				nnk_textedit_paste(edit, text, nnk_strlen(text));
			}
		});

	}

	public void destroy(long win) {
		glfwFreeCallbacks(win);
	}

	public void setCurrent(Scene scene) {
		currentScene = scene;
	}

	public ControlsSettings getKeys() {
		return keys;
	}
}

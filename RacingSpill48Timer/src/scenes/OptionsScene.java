package scenes;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;

import adt.Scene;
import adt.Visual;
import audio.Audio;
import handlers.GameHandler;
import handlers.SceneHandler;
import main.RaceKeysSettings;
import main.RegularSettings;
import window.Windows;

public class OptionsScene extends Scene {
	private JButton goBack;
	private JButton nextSong;
	private JButton stopMusic;
	private JButton togglefullscreen;
	private JButton specifyRes;
	private JLabel specifyResLabel;
	private JLabel masterVolumeLabel;
	private JLabel sfxVolumeLabel;
	private JLabel musicVolumeLabel;
	private JSlider masterVolumeSlider;
	private JSlider sfxVolumeSlider;
	private JSlider musicVolumeSlider;
	protected BufferedImage controlsImage;

	public OptionsScene() {
		super("options");

		try {
			controlsImage = ImageIO.read(getClass().getResourceAsStream("/pics/controls.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		goBack = new JButton("Go back");
		nextSong = new JButton("Play another song instead!");
		stopMusic = new JButton("Turn off/on that music!!!");
		masterVolumeLabel = new JLabel("<html><font color='white'>" + "Master-Volume:" + "</font></html>");
		sfxVolumeLabel = new JLabel("<html><font color='white'>" + "Sounds:" + "</font></html>");
		musicVolumeLabel = new JLabel("<html><font color='white'>" + "Music:" + "</font></html>");
		togglefullscreen = new JButton("Fullscreen: " + true);
		specifyRes = new JButton("Specify resolution");
		specifyResLabel = new JLabel(
				"<html><font color='white'>" + "Specified resolution: " + false + "</font></html>");

		masterVolumeLabel.setPreferredSize(new Dimension(150, 20));

		masterVolumeSlider = new JSlider(SwingConstants.HORIZONTAL, 0, 100,
				(int) (GameHandler.getMasterVolume() * 100.0));
		masterVolumeSlider.setMinorTickSpacing(1);
		masterVolumeSlider.setMajorTickSpacing(10);
		masterVolumeSlider.setPaintTicks(true);
		masterVolumeSlider.setPaintLabels(true);

		masterVolumeSlider.addChangeListener((ChangeEvent e) -> {
			JSlider source = (JSlider) e.getSource();

			GameHandler.setMasterVolume(source.getValue() / 100.0);
			GameHandler.music.updateVolume();
		});

		sfxVolumeSlider = new JSlider(SwingConstants.HORIZONTAL, 0, 100, (int) (GameHandler.getSfxVolume() * 100.0));
		sfxVolumeSlider.setMinorTickSpacing(1);
		sfxVolumeSlider.setMajorTickSpacing(10);
		sfxVolumeSlider.setPaintTicks(true);
		sfxVolumeSlider.setPaintLabels(true);

		sfxVolumeSlider.addChangeListener((ChangeEvent e) -> {
			JSlider source = (JSlider) e.getSource();

			GameHandler.setSfxVolume(source.getValue() / 100.0);
		});

		musicVolumeSlider = new JSlider(SwingConstants.HORIZONTAL, 0, 100,
				(int) (GameHandler.getMusicVolume() * 100.0));
		musicVolumeSlider.setMinorTickSpacing(1);
		musicVolumeSlider.setMajorTickSpacing(10);
		musicVolumeSlider.setPaintTicks(true);
		musicVolumeSlider.setPaintLabels(true);

		musicVolumeSlider.addChangeListener((ChangeEvent e) -> {
			JSlider source = (JSlider) e.getSource();

			GameHandler.setMusicVolume(source.getValue() / 100.0);
			GameHandler.music.updateVolume();
		});

		togglefullscreen.addActionListener((ActionEvent e) -> {
			boolean temp = !SceneHandler.instance.isFullScreen();
			SceneHandler.instance.setFullScreen(temp);
			togglefullscreen.setText("Fullscreen: " + temp);
			if (!temp) {
				specifyRes.setEnabled(false);
				Object[] possibilities = { 720, 1080 };
				SceneHandler.instance.setHEIGHT((int) JOptionPane.showInputDialog(null, "Choose resolution",
						"Carznstuff", JOptionPane.PLAIN_MESSAGE, null, possibilities, 720));
				SceneHandler.instance.setSpecified(false);
				specifyResLabel.setText("Specified resolution: " + false);
			} else {
				specifyRes.setEnabled(true);
			}
		});

		specifyRes.addActionListener((ActionEvent e) -> {
			int sure = JOptionPane.showConfirmDialog(null, "Do you want the fullscreen resolution specified?");
			if (sure == 0) {
				SceneHandler.instance.setSpecified(true);
				String res;
				int result = 0;
				do {
					res = JOptionPane.showInputDialog("Write height in px (1080 or 720):");

					try {
						result = Integer.valueOf(res);
					} catch (Exception ex) {

						if (ex.getMessage().equals("null"))
							return;
						res = null;
						JOptionPane.showMessageDialog(null, "Not correctly done, sir");
					}

				} while (res == null);
				SceneHandler.instance.setHEIGHT(result);
				specifyResLabel.setText("Specified resolution: " + SceneHandler.instance.HEIGHT + "p");
			} else if (sure == 1) {
				SceneHandler.instance.setSpecified(false);
				specifyResLabel.setText("Specified resolution: " + false);
			} else {
				return;
			}

		});
		goBack.addActionListener((ActionEvent e) -> {
			SceneHandler.instance.changeScene(SceneHandler.instance.getLastScene());
			GameHandler.ba.playRegularBtn();
		});
		nextSong.addActionListener((ActionEvent e) -> GameHandler.music.playNext());
		stopMusic.addActionListener((ActionEvent e) -> GameHandler.music.playOrStop());

		add(goBack);
		add(nextSong);
		add(stopMusic);
		add(masterVolumeLabel);
		add(masterVolumeSlider);
		add(sfxVolumeLabel);
		add(sfxVolumeSlider);
		add(musicVolumeLabel);
		add(musicVolumeSlider);
		add(togglefullscreen);
		add(specifyResLabel);
		add(specifyRes);
	}
	

	public void init(RegularSettings settings, RaceKeysSettings keys, Audio audio) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.drawImage(backgroundImage, 0, 0, Windows.WIDTH, Windows.HEIGHT, null);
		g.drawImage(controlsImage, 10, Windows.HEIGHT - (int) (controlsImage.getHeight() * 1.1),
				controlsImage.getWidth(), controlsImage.getHeight(), null);
	}

}

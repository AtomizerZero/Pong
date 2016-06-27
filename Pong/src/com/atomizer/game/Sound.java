package com.atomizer.game;

import javax.sound.sampled.*;

public class Sound {

	private Clip clip;

	public Sound(String name) {
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(getClass().getResource(name));
			AudioFormat baseFormat = ais.getFormat();
			AudioFormat decodeFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16,
					baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
			AudioInputStream dais = AudioSystem.getAudioInputStream(decodeFormat, ais);
			clip = AudioSystem.getClip();
			clip.open(dais);
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

	public void play() {
		if (clip == null)
			return;
		stop();
		clip.setFramePosition(0);
		clip.start();
	}

	private void stop() {
		if (clip.isRunning())
			clip.stop();

	}

	public void close() {
		stop();
		clip.close();
	}

}

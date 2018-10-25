package imageComparer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import javafx.stage.FileChooser;

public class comparer {
	BufferedImage image1 = null;
	BufferedImage image2 = null;
	JLabel label1 = null;
	JLabel label2 = null;
	int count = 0;
	int difference = 0;
	
	public void setImage1(BufferedImage image) { this.image1 = image; }
	public void setImage2(BufferedImage image) { this.image2 = image; }
	public void setLabel1(JLabel label) { this.label1 = label; }
	public void setLabel2(JLabel label) { this.label2 = label; }
	public void setDiff(int diff) { this.difference = diff; }
	public BufferedImage getImage1() { return image1; }
	public BufferedImage getImage2() { return image2; }
	public JLabel getLabel1() { return label1; }
	public JLabel getLabel2() { return label2; }
	public int getDiff() { return difference; }

	
	
	public void createGUI() {
		JFrame frame = new JFrame("Image Comparer");
		frame.setSize(1000, 500);
		
		JMenuBar mb = new JMenuBar();
		JMenu m1 = new JMenu("File");
		JMenu m2 = new JMenu("Help");
		
		JMenuItem m11 = new JMenuItem("Upload Image");
		JMenuItem m12 = new JMenuItem("Reset");
		JMenuItem m21 = new JMenuItem("Instructions");
		JMenuItem m22 = new JMenuItem("About");
		
		mb.add(m1);
		mb.add(m2);
		m1.add(m11);
		m1.add(m12);
		m2.add(m21);
		m2.add(m22);
		
		JButton button = new JButton("Compare");
		
		final JFileChooser fc = new JFileChooser();
		
		m11.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fc.addChoosableFileFilter(new FileNameExtensionFilter("Image files",
				          new String[] { "png", "jpg", "jpeg", "gif" }));
				if(fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					try {
						BufferedImage test = ImageIO.read(fc.getSelectedFile());
						chooseImage(test);
						frame.repaint();
						frame.revalidate();
					}
					catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		});
		
		m12.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(label1);
				frame.remove(label2);
			}
		});
		
		m21.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		m22.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(count != 0) {
					frame.remove(label1);
					frame.remove(label2);
				}
				addImageOne(getLabel1(), frame);
				addImageTwo(getLabel2(), frame);
				testImage(getImage1(), getImage2());
				count++;
				frame.repaint();
				frame.revalidate();
			}
		});
		
		frame.getContentPane().add(BorderLayout.SOUTH, button);
		frame.getContentPane().add(BorderLayout.NORTH, mb);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
	}
	
	public void chooseImage(BufferedImage image) {
		JFrame frame = new JFrame("Choose Image");
		frame.setSize(300, 200);
		JButton button1 = new JButton("Image 1");
		JButton button2 = new JButton("Image 2");
		frame.getContentPane().add(BorderLayout.WEST, button1);
		frame.getContentPane().add(BorderLayout.EAST, button2);
		frame.setVisible(true);
		button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setImage1(image);
				ImageIcon icon1 = new ImageIcon((Image)getImage1());
				JLabel label1 = new JLabel();
				label1.setIcon(icon1);
				setLabel1(label1);
				frame.dispose();
			}
		});
		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setImage2(image);
				ImageIcon icon1 = new ImageIcon((Image)getImage2());
				JLabel label1 = new JLabel();
				label1.setIcon(icon1);	
				setLabel2(label1);
				frame.dispose();
			}
		});
	}
	
	/**
	 * Converts Image to BufferedImage for manipulation
	 * Credit: Sri Harsha Chilakapati on StackOverflow
	 * @param img
	 * @return Buffered Image 
	 */
	public static BufferedImage toBufferedImage(Image img) {
		if(img instanceof BufferedImage) {
			return (BufferedImage)img;
		}
		
		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();
		
		return bimage;
	}
	
	public void addImageOne(JLabel label, JFrame frame) {
		frame.getContentPane().add(BorderLayout.WEST, label);
	}
	
	public void addImageTwo(JLabel label, JFrame frame) {
		frame.getContentPane().add(BorderLayout.EAST, label);
	}
	
	public void showResult(boolean res) {
		JFrame frame = new JFrame("Result");
		frame.setSize(300, 50);
		if(res == true) {
			JLabel label1 = new JLabel("The images are the same");
			frame.getContentPane().add(BorderLayout.CENTER, label1);
		}
		else {
			JLabel label2 = new JLabel("The images are not the same");
			frame.getContentPane().add(BorderLayout.CENTER, label2);
		}
		frame.setVisible(true);
	}
	
	public void testImage(Image image1, Image image2) {
		BufferedImage image1B = toBufferedImage(image1);
		BufferedImage image2B = toBufferedImage(image2);
		int total = 0;
		boolean sameSize = false;
		
		if(image1B.getHeight() == image2B.getHeight() && image1B.getWidth() == image2B.getWidth()) {
			sameSize = true;
			for(int i = 0; i < image1B.getWidth(); i++) {
				for(int j = 0; j < image1B.getHeight(); j++) {
					total += Math.pow(image1B.getRGB(i, j) - image2B.getRGB(i, j), 2);
				}
			}
		}
		else {
			showResult(false);
		}
		
		if(total == 0 && sameSize == true) {
			showResult(true);
		}
		else {
			showResult(false);
		}
	}
	
}

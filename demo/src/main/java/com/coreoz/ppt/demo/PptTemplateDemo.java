package com.coreoz.ppt.demo;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.poi.util.IOUtils;

import com.coreoz.ppt.PptImageReplacementMode;
import com.coreoz.ppt.PptMapper;

public class PptTemplateDemo {

	public static void main(String[] args) throws IOException {
		try(FileOutputStream out = new FileOutputStream("generated.pptx")) {
			new PptMapper()
				.hide("hidden", arg -> "true".equals(arg))
				.text("var1", "Content replaced")
				.text("var3", "Header cell replaced")
				.text("var4", "Content cell replaced")
				.image("image1", IOUtils.toByteArray(PptTemplateDemo.class.getResourceAsStream("/images/replacedImage.jpg")))
				.image(
					"image2",
					IOUtils.toByteArray(PptTemplateDemo.class.getResourceAsStream("/images/replacedImage.jpg")),
					PptImageReplacementMode.RESIZE_ONLY
				)
				.styleText("style", textRun -> {
					textRun.setBold(true);
					textRun.setFontColor(Color.GREEN);
				})
				.styleShape("percentage", shape -> {
					Rectangle2D shapeAnchor = shape.getAnchor();
					shape.setAnchor(new Rectangle2D.Double(
						shapeAnchor.getX(),
						shapeAnchor.getY(),
						// widen the shape of 20%
						shapeAnchor.getWidth() * 1.2,
						shapeAnchor.getHeight()
					));
				})
				.processTemplate(PptTemplateDemo.class.getResourceAsStream("/template.pptx"))
				.write(out);
		}
		System.out.println("Templated presentation generated in " + Paths.get("generated.pptx").toAbsolutePath());
	}

}

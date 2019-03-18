package edu.psgv.sweng861;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class models a media playlist ending in .ts
 *
 * @author Kyle Hughes
 */
public class MediaPlaylist extends Playlist{
	private static final Logger logger = LogManager.getLogger();

	/**
	 * Constructor for creating a media Playlist with the given contents and url
	 */
   public MediaPlaylist(List<String> contents,String url) {
	  super(url);
      this.contents = contents;
      type="Simple Playlist";
   }

   /**
    * printReport() Media(simple) playlists reports are basic and will only show the errors
    */
   @Override
   public void printReport(boolean tab) {
		logger.debug(">>printReport() media playlist");
         simpleReport(tab);
 		logger.debug("<<printReport() media playlist");

   }
}
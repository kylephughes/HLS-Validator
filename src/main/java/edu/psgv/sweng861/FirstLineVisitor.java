package edu.psgv.sweng861;

import java.util.List;

/**
 * FirstLineVisitor - Checks the first line of each playlist file and saves
 * 					  any errors
 * @author Kyle Hughes
 *
 */
public class FirstLineVisitor extends HLSVisitor {

	/**
	 * visitMedia() check the first line of each simple playlist
	 * @param pl Simple playlist
	 */
	@Override
	public void visitMedia(MediaPlaylist pl) {
		// make sure the playlist has a valid first line
		logger.debug(">>visit FirstLine()");
	    firstLine(pl);
		logger.debug("<<visit FirstLine()");
	}

	/**
	 * visitMaster() Check the first line of the master playlist and its variants
	 */
	@Override
	public void visitMaster(MasterPlaylist pl) {
		// make sure the playlist has a valid first line
		logger.debug(">>visit FirstLine master()");
        firstLine(pl);
		//verify the variants 
		for(MediaPlaylist media : pl.getVariants()) {
			visitMedia(media);
		}
		logger.debug("<<visit FirstLine master()");
	}

	/**
	 * firstLine() Checks the first line in both master and media playlists
	 * @param pl
	 */
	private void firstLine(Playlist pl) {
		logger.debug(">>firstLine ()" + pl.getUrl());
		List<String> contents = pl.getContent();

		if (contents.size() < 1) {
			pl.addError(PlaylistFactory.getErrorObj(ErrorType.FATAL,0, "Problem reading the contents of the URL, it was skipped"));

			logger.error("<<visit firstLine()  no contents found");
		} else if (contents.size() > 0) {
			if (contents.get(0).equals("#EXTM3U")) {
				logger.info("<<visit firstLine() returned true validated");
			} else {
				// create new error obj to add onto the playlist
				pl.addError(PlaylistFactory.getErrorObj(ErrorType.MAJOR,1, "Playlist does not start with #EXTM3U"));
				logger.info("<<visit FirstLine() returned false, does not start with #EXTM3U");
			}
		}	
		
		logger.debug("<<firstLine ()" + pl.getUrl());

	}

}

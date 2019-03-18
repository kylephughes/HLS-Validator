package edu.psgv.sweng861;

import java.util.List;

/**
 * TagURIVisitor - Check each URI Tag and validate the URI for Simple 
 * 					and Master Playlists
 * @author Kyle Hughes
 *
 */
public class TagUriVisitor extends HLSVisitor{
	//represent URI Tags
	private static enum StreamType {EXTINF, EXTXSTREAMINF};
	/**
	 * visitMedia() Verify each Simple playlist has valid URI Tags and URI's
	 */
	@Override
	public void visitMedia(MediaPlaylist pl) {
		logger.debug(">> visitMedia() URIVisitor");
		StreamType type = StreamType.EXTINF;
		boolean nextLine = false;
		List<String> contents = pl.getContent();
		int lineCount = 0;
		for(String line : contents) {
			lineCount++;
			if(nextLine) {
				verifyNextLine(type,line,pl,lineCount);
				//reset for next 
				nextLine=false;
			} else {
				//check for tags
				if(line.startsWith("#EXTINF")) {
					nextLine = true;
				} else if(line.startsWith("#EXT-X-STREAM-INF")){				
					PlaylistError error = PlaylistFactory.getErrorObj(ErrorType.MAJOR,lineCount,
							"Found - #EXT-X-STREAM-INF, this tag must not be in a media playlist");
					pl.addError(error);
					//will check for a variant stream in the nextLine, even if its a simple playlist
					type = StreamType.EXTXSTREAMINF;
					logger.debug("Found a variant stream tag in a simple playlist");
					nextLine =true;
				}
			}
		}
		logger.debug("<< visitMedia() URIVisitor");

	}
	
	/**
	 * verifyNextLine() Helper method to check the next line for both simple and master
	 * 					playlists. Report error if the next line is not an appropriate 
	 * 					URI based on the previous tag. If conflicting tags exist, it will check
	 * 					if the URI follows the tag found first, followed by the expected type of the 
	 * 					playlist (variant or media segement)
	 * @param stream the type we found on previous line(simple or master) 
	 * @param line	the next line to verify
	 * @param pl	the playlist currently processing
	 * @param lineCount	the line in the file we are on
	 */

	private void verifyNextLine(StreamType stream, String line, Playlist pl, int lineCount) {
		logger.debug(">> verifyNextLine() " + lineCount);
		String plType = pl.getType();
		String description = "";
		PlaylistError error;
		//fatal error unless the URI matches the rest of the playlist type
		ErrorType errorType = ErrorType.FATAL;
		
		if(stream == StreamType.EXTINF) {
			if(!line.endsWith(".ts")) {
				description = "#EXTINF is not followed by a media segment URI";
				if(plType.equals("MASTER") && line.endsWith("m3u8")) {
					description += ", however it is a valid variant stream ";
					//minor because we are looking at a master playlist and found a variant, otherwise its fatal
					errorType = ErrorType.MINOR;
					logger.error("Found a variant stream when expecting a media segement from the previous tag");
				}
				error = PlaylistFactory.getErrorObj(errorType,lineCount,description);
				pl.addError(error);
			}
		} else {
			//stream is looking for a variant
			if(!line.endsWith(".m3u8")) {
				description = "#EXT-X-STREAM-INF is not followed by a variant URI";
				if(plType.equals("Simple Playlist") && line.endsWith(".ts")) {
					description += ", however it is a valid media segment";
					//its minor because we are looking at a simple playlist and found a media segment, otherwise its fatal
					errorType = ErrorType.MINOR;
					logger.error("Found a media segment when expecting a variant stream from the previous tag");
				}
				error = PlaylistFactory.getErrorObj(errorType,lineCount,description);
				pl.addError(error);
			}
		}
		logger.debug("<< verifyNextLine()");
	}

	/**
	 * visitMaster() Verify the master playlist has valid URI Tags and URIS
	 */
	@Override
	public void visitMaster(MasterPlaylist pl) {
		logger.debug(">> visitMaster() URIVisitor");
		StreamType type = StreamType.EXTXSTREAMINF;
		boolean nextLine = false;
		List<String> contents = pl.getContent();
		int lineCount = 0;
		for(String line : contents) {
			lineCount++;
			//previous line had a URI tag so check the URI
			if(nextLine) {
				verifyNextLine(type,line,pl,lineCount);
				//reset for next 
				nextLine=false;
			} else {
				//check for tags
				if(line.startsWith("#EXT-X-STREAM-INF")) {
					nextLine = true;
					//   MUST reject Playlists that contain both Media Segment Tags and Master
					//  Playlist tags
				} else if(line.startsWith("#EXTINF")){				
					PlaylistError error = PlaylistFactory.getErrorObj(ErrorType.MAJOR,lineCount,
											"Found - #EXTINF, this tag must not be in a master playlist");
					pl.addError(error);				
					//found a bad tag in master playlist but still check for media segment
					type = StreamType.EXTINF;
					logger.debug("Found a media segment tag in a master playlist");
					nextLine =true;
				}
			}
		}
		logger.debug("<< visitMaster() URIVisitor");
	}
}

package se.atrosys.perft.node.work.worker;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import se.atrosys.perft.common.work.ResultItem;
import se.atrosys.perft.common.work.WorkItem;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A Spider/Monkey worker will fetch the requested URI and suggest a follow-up to the spawner.
 */
public class SpiderMonkeyWorker extends AbstractWorker {
	private URI nextUri;
	public SpiderMonkeyWorker(PoolingHttpClientConnectionManager connectionManager) {
		super(connectionManager);
	}

	public URI getNextUri() {
		return nextUri;
	}

	@Override
	protected void doActualWork(StringBuilder builder, ResultItem resultItem) {
		List<URI> uris = findWorthwhileURIs(builder);

		if (uris.isEmpty()) {
			nextUri = null;
		} else {
			nextUri = uris.get(new Random(System.currentTimeMillis()).nextInt(uris.size()));
		}
	}

	// TODO this should probably be in a class of its own
	protected List<URI> findWorthwhileURIs(StringBuilder builder) {
		List<URI> uris = new ArrayList<URI>();

		Document doc = Jsoup.parse(builder.toString());
		Elements links = doc.select("a[href]");

		for (Element e: links) {
			try {
				URI href = new URI(e.attr("href").trim());

				// TODO this needs much, much more work.
				if (href.isAbsolute()) {
					uris.add(href);
				}
			} catch (URISyntaxException e1) {
				logger.debug("Could not create a URI from {}", e.attr("href").trim());
			}
		}

		return uris;
	}
}

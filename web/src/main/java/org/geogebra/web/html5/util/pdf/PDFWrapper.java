package org.geogebra.web.html5.util.pdf;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Wrapper class for pdf.js
 * 
 * @author laszlo
 *
 */
public class PDFWrapper {

	private PDFListener listener;
	private int pageCount;
	private int pageNumber = 1;
	private JavaScriptObject pdf = null;

	/**
	 * Interface to communicate with PDF Container.
	 *
	 */
	public interface PDFListener {
		/**
		 * Call this to build image from pdf.
		 * 
		 * @param imgSrc
		 *            the image data as source.
		 */
		void onPageDisplay(String imgSrc);

		/**
		 * After the pdf loaded, the progress bar should be finished quickly.
		 * 
		 * @param result
		 *            true if the loading of the pdf was successful
		 */
		void finishLoading(boolean result);

	}

	/**
	 * Constructor
	 * 
	 * @param file
	 *            PDF to handle.
	 * @param listener
	 *            to communicate with PDF container.
	 */
	public PDFWrapper(JavaScriptObject file, PDFListener listener) {
		this.listener = listener;
		read(file);
	}

	private void finishLoading(boolean result) {
		listener.finishLoading(result);
	}

	private native void read(JavaScriptObject file) /*-{
		if (!file) {
			file = $doc.querySelector('input[type=file]').files[0];
		}
		var reader = new FileReader();
		var that = this;

		reader
				.addEventListener(
						"load",
						function() {
							var src = reader.result;
							that.@org.geogebra.web.html5.util.pdf.PDFWrapper::load(Ljava/lang/String;)(src);
						}, false);

		if (file) {
			reader.readAsDataURL(file);
		}

	}-*/;

	private native void load(String src) /*-{
		var progressCallback = function(progress) {
			@org.geogebra.common.util.debug.Log::debug(Ljava/lang/String;)("total: " + progress.total + ", loaded: " + progress.loaded);
		}

		var loadingTask = $wnd.PDFJS.getDocument(src, null, null,
				progressCallback);
		var that = this;

		loadingTask.promise
				.then(
						function(pdf) {
							@org.geogebra.common.util.debug.Log::debug(Ljava/lang/Object;)('PDF loaded');
							that.@org.geogebra.web.html5.util.pdf.PDFWrapper::setPdf(Lcom/google/gwt/core/client/JavaScriptObject;)(pdf);
							that.@org.geogebra.web.html5.util.pdf.PDFWrapper::setPageCount(I)(pdf.numPages);
							that.@org.geogebra.web.html5.util.pdf.PDFWrapper::finishLoading(Z)(true);
						},
						function(reason) {
							// PDF loading error
							@org.geogebra.common.util.debug.Log::error(Ljava/lang/String;)(reason);
							that.@org.geogebra.web.html5.util.pdf.PDFWrapper::finishLoading(Z)(false);
						});
	}-*/;

	private native void renderPage() /*-{
		var that = this;
		var pdf = this.@org.geogebra.web.html5.util.pdf.PDFWrapper::pdf;
		var pageNumber = this.@org.geogebra.web.html5.util.pdf.PDFWrapper::pageNumber;
		pdf
				.getPage(pageNumber)
				.then(
						function(page) {
							@org.geogebra.common.util.debug.Log::debug(Ljava/lang/Object;)('Page loaded');

							var scale = 1;
							var viewport = page.getViewport(scale);

							return page
									.getOperatorList()
									.then(
											function(opList) {
												var svgGfx = new $wnd.PDFJS.SVGGraphics(
														page.commonObjs,
														page.objs);
												return svgGfx
														.getSVG(opList,
																viewport)
														.then(
																function(svg) {
																	svgs = (new XMLSerializer())
																			.serializeToString(svg);
																	// convert to base64 URL for <img>
																	var data = "data:image/svg+xml;base64,"
																			+ btoa(unescape(encodeURIComponent(svgs)));
																	that.@org.geogebra.web.html5.util.pdf.PDFWrapper::onPageDisplay(Ljava/lang/String;)(data);
																});
											});
						});
	}-*/;

	private void onPageDisplay(String src) {
		if (listener == null) {
			return;
		}
		listener.onPageDisplay(src);
	}

	/**
	 * 
	 * @return the number of pages in the PDF.
	 */
	public int getPageCount() {
		return pageCount;
	}

	/**
	 * 
	 * @param pageCount
	 *            to set.
	 */
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	/**
	 * 
	 * @return PDF as JavaScriptObject
	 */
	public JavaScriptObject getPdf() {
		return pdf;
	}

	/**
	 * sets PDF as JavaScriptObject
	 */
	public void setPdf(JavaScriptObject pdf) {
		this.pdf = pdf;
	}

	/**
	 * load previous page of the PDF if any.
	 */
	public void previousPage() {
		if (pageNumber > 1) {
			setPageNumber(pageNumber - 1);
		}
	}

	/**
	 * load next page of the PDF if any.
	 */
	public void nextPage() {
		if (pageNumber < pageCount) {
			setPageNumber(pageNumber + 1);
		}
	}

	/**
	 * 
	 * @return the current page index.
	 */
	public int getPageNumber() {
		return pageNumber;
	}

	/**
	 * 
	 * @param num
	 *            page number to set.
	 * @return if page change was successful.
	 */
	public boolean setPageNumber(int num) {
		if (num > 0 && num <= pageCount) {
			pageNumber = num;
			renderPage();
			return true;
		}
		return false;
	}
}
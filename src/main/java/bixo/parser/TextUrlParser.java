/*
 * Copyright (c) 1997-2009 101tec Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy 
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights 
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell 
 * copies of the Software, and to permit persons to whom the Software is 
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in 
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */
package bixo.parser;

import bixo.Constants;
import bixo.fetcher.FetchStatus;
import bixo.items.UrlItem;
import cascading.flow.FlowProcess;
import cascading.operation.BaseOperation;
import cascading.operation.Function;
import cascading.operation.FunctionCall;
import cascading.tuple.Tuple;
import cascading.tuple.TupleEntry;

@SuppressWarnings("serial")
public class TextUrlParser extends BaseOperation<String> implements Function<String> {

    private UrlFilter[] _urlFilters = new UrlFilter[0];

    public TextUrlParser() {
        super(UrlItem.FIELDS);
    }

    public TextUrlParser(UrlFilter... urlFilters) {
        super(UrlItem.FIELDS);
        if (urlFilters != null) {
            _urlFilters = urlFilters;
        }
    }

    @Override
    public void operate(FlowProcess process, FunctionCall<String> call) {
        TupleEntry arguments = call.getArguments();
        String url = (String) arguments.get(Constants.LINE);

        for (UrlFilter filter : _urlFilters) {
            url = filter.filter(url);
            if (url == null) {
                // ignore this url
                return;
            }
        }
        // emit link with default values
        call.getOutputCollector().add(new Tuple(url, System.currentTimeMillis(), 0, FetchStatus.NEVER_FETCHED.ordinal()));
    }
}
package utils;

import flexjson.transformer.AbstractTransformer;

public class FirstSentenceTransformer extends AbstractTransformer {

	@Override
	public void transform(Object arg0) {
		if (arg0 != null) {
			getContext().writeQuoted(TextUtils.extractSentence((String) arg0, 1));
		}	
	}
}

package utils;

import flexjson.transformer.AbstractTransformer;

public class HasPictureTransformer extends AbstractTransformer {

	@Override
	public void transform(Object arg0) {
		getContext().write(String.valueOf(arg0 != null));
	}
}


var JSCrypto = {
	encryptByAES : function(message, key) {
	
		var keyHex = CryptoJS.enc.Utf8.parse(key);
		var encrypted = CryptoJS.TripleDES.encrypt(message, keyHex, {
			mode : CryptoJS.mode.ECB,
			padding : CryptoJS.pad.Pkcs7
		});
		return encrypted.toString();
	},
	decryptByAES : function(message, key) {
		
		var keyHex = CryptoJS.enc.Utf8.parse(key);
		var decrypted = CryptoJS.TripleDES.decrypt({
			ciphertext : CryptoJS.enc.Base64.parse(message)
		}, keyHex, {
			mode : CryptoJS.mode.ECB,
			padding : CryptoJS.pad.Pkcs7
		});
		return decrypted.toString(CryptoJS.enc.Utf8);
	}

};
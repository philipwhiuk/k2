package com.fsck.k9.view;


import android.support.annotation.AttrRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.fsck.k9.R;
import com.fsck.k9.mailstore.CryptoResultAnnotation;
import org.openintents.openpgp.OpenPgpDecryptionResult;
import org.openintents.openpgp.OpenPgpSignatureResult;
import org.openintents.smime.SMimeDecryptionResult;
import org.openintents.smime.SMimeSignatureResult;


public enum MessageCryptoDisplayStatus {
    LOADING (
            R.attr.crypto_grey,
            R.drawable.status_lock
    ),

    CANCELLED (
            R.attr.crypto_black,
            R.drawable.status_lock,
            R.string.crypto_msg_cancelled
    ),

    DISABLED (
            R.attr.crypto_grey,
            R.drawable.status_lock_disabled,
            R.string.crypto_msg_disabled
    ),

    UNENCRYPTED_SIGN_UNKNOWN (
            R.attr.crypto_black,
            R.drawable.status_signature_unverified_cutout, R.drawable.status_dots,
            R.string.crypto_msg_signed_unencrypted, R.string.crypto_msg_sign_unknown
    ),

    UNENCRYPTED_SIGN_VERIFIED (
            R.attr.crypto_blue,
            R.drawable.status_signature_verified_cutout, R.drawable.status_none_dots_3,
            R.string.crypto_msg_signed_unencrypted, R.string.crypto_msg_sign_verified
    ),
    UNENCRYPTED_SIGN_UNVERIFIED (
            R.attr.crypto_orange,
            R.drawable.status_signature_verified_cutout, R.drawable.status_none_dots_2,
            R.string.crypto_msg_signed_unencrypted, R.string.crypto_msg_sign_unverified
    ),
    UNENCRYPTED_SIGN_MISMATCH (
            R.attr.crypto_red,
            R.drawable.status_signature_verified_cutout, R.drawable.status_none_dots_1,
            R.string.crypto_msg_signed_unencrypted, R.string.crypto_msg_sign_mismatch
    ),
    UNENCRYPTED_SIGN_EXPIRED (
            R.attr.crypto_red,
            R.drawable.status_signature_verified_cutout, R.drawable.status_none_dots_1,
            R.string.crypto_msg_signed_unencrypted, R.string.crypto_msg_sign_expired
    ),
    UNENCRYPTED_SIGN_REVOKED (
            R.attr.crypto_red,
            R.drawable.status_signature_verified_cutout, R.drawable.status_none_dots_1,
            R.string.crypto_msg_signed_unencrypted, R.string.crypto_msg_sign_revoked
    ),
    UNENCRYPTED_SIGN_INSECURE (
            R.attr.crypto_red,
            R.drawable.status_signature_verified_cutout, R.drawable.status_none_dots_1,
            R.string.crypto_msg_signed_unencrypted, R.string.crypto_msg_sign_insecure
    ),
    UNENCRYPTED_SIGN_ERROR (
            R.attr.crypto_red,
            R.drawable.status_signature_verified_cutout, R.drawable.status_dots,
            R.string.crypto_msg_signed_error, null
    ),

    ENCRYPTED_SIGN_UNKNOWN (
            R.attr.crypto_black,
            R.drawable.status_lock_opportunistic, R.drawable.status_dots,
            R.string.crypto_msg_signed_encrypted, R.string.crypto_msg_sign_unknown
    ),

    ENCRYPTED_SIGN_VERIFIED (
            R.attr.crypto_green,
            R.drawable.status_lock, R.drawable.status_none_dots_3,
            R.string.crypto_msg_signed_encrypted, R.string.crypto_msg_sign_verified
    ),
    ENCRYPTED_SIGN_UNVERIFIED (
            R.attr.crypto_orange,
            R.drawable.status_lock, R.drawable.status_none_dots_2,
            R.string.crypto_msg_signed_encrypted, R.string.crypto_msg_sign_unverified
    ),
    ENCRYPTED_SIGN_MISMATCH (
            R.attr.crypto_red,
            R.drawable.status_lock, R.drawable.status_none_dots_1,
            R.string.crypto_msg_signed_encrypted, R.string.crypto_msg_sign_mismatch
    ),
    ENCRYPTED_SIGN_EXPIRED (
            R.attr.crypto_red,
            R.drawable.status_lock, R.drawable.status_none_dots_1,
            R.string.crypto_msg_signed_encrypted, R.string.crypto_msg_sign_expired
    ),
    ENCRYPTED_SIGN_REVOKED (
            R.attr.crypto_red,
            R.drawable.status_lock, R.drawable.status_none_dots_1,
            R.string.crypto_msg_signed_encrypted, R.string.crypto_msg_sign_revoked
    ),
    ENCRYPTED_SIGN_INSECURE (
            R.attr.crypto_red,
            R.drawable.status_lock, R.drawable.status_none_dots_1,
            R.string.crypto_msg_signed_encrypted, R.string.crypto_msg_sign_insecure
    ),
    ENCRYPTED_UNSIGNED (
            R.attr.crypto_red,
            R.drawable.status_lock, R.drawable.status_dots,
            R.string.crypto_msg_encrypted_unsigned, R.string.crypto_msg_unsigned_encrypted
    ),
    ENCRYPTED_SIGN_ERROR (
            R.attr.crypto_red,
            R.drawable.status_lock, R.drawable.status_dots,
            R.string.crypto_msg_signed_encrypted, R.string.crypto_msg_sign_error
    ),

    ENCRYPTED_ERROR (
            R.attr.crypto_red,
            R.drawable.status_lock_error,
            R.string.crypto_msg_encrypted_error
    ),

    INCOMPLETE_ENCRYPTED (
            R.attr.crypto_black,
            R.drawable.status_lock_opportunistic,
            R.string.crypto_msg_incomplete_encrypted
    ),
    INCOMPLETE_SIGNED (
            R.attr.crypto_black,
            R.drawable.status_signature_unverified_cutout, R.drawable.status_dots,
            R.string.crypto_msg_signed_unencrypted, R.string.crypto_msg_sign_incomplete
    ),

    UNSUPPORTED_ENCRYPTED (
            R.attr.crypto_red,
            R.drawable.status_lock_error,
            R.string.crypto_msg_unsupported_encrypted
    ),
    UNSUPPORTED_SIGNED (
            R.attr.crypto_grey,
            R.drawable.status_lock_disabled,
            R.string.crypto_msg_unsupported_signed
    ),
    ;

    @AttrRes public final int colorAttr;

    @DrawableRes public final int statusIconRes;
    @DrawableRes public final Integer statusDotsRes;

    @StringRes public final Integer textResTop;
    @StringRes public final Integer textResBottom;

    MessageCryptoDisplayStatus(@AttrRes int colorAttr, @DrawableRes int statusIconRes, @DrawableRes Integer statusDotsRes,
            @StringRes int textResTop, @StringRes Integer textResBottom) {
        this.colorAttr = colorAttr;
        this.statusIconRes = statusIconRes;
        this.statusDotsRes = statusDotsRes;

        this.textResTop = textResTop;
        this.textResBottom = textResBottom;
    }

    MessageCryptoDisplayStatus(@AttrRes int colorAttr, @DrawableRes int statusIconRes, @StringRes int textResTop) {
        this.colorAttr = colorAttr;
        this.statusIconRes = statusIconRes;
        this.statusDotsRes = null;

        this.textResTop = textResTop;
        this.textResBottom = null;
    }

    MessageCryptoDisplayStatus(@AttrRes int colorAttr, @DrawableRes int statusIconRes) {
        this.colorAttr = colorAttr;
        this.statusIconRes = statusIconRes;
        this.statusDotsRes = null;

        this.textResTop = null;
        this.textResBottom = null;
    }

    @NonNull
    public static MessageCryptoDisplayStatus fromResultAnnotation(CryptoResultAnnotation cryptoResult) {
        if (cryptoResult == null) {
            return DISABLED;
        }

        switch (cryptoResult.getErrorType()) {
            case OPENPGP_OK:
                return getDisplayStatusForPgpResult(cryptoResult);

            case SMIME_OK:
                return getDisplayStatusForSMimeResult(cryptoResult);

            case ENCRYPTED_BUT_INCOMPLETE:
                return INCOMPLETE_ENCRYPTED;

            case SIGNED_BUT_INCOMPLETE:
                return INCOMPLETE_SIGNED;

            case ENCRYPTED_BUT_UNSUPPORTED:
                return UNSUPPORTED_ENCRYPTED;

            case SIGNED_BUT_UNSUPPORTED:
                return UNSUPPORTED_SIGNED;

            case UI_CANCELED:
                return CANCELLED;

            case OPENPGP_API_RETURNED_ERROR:
                return ENCRYPTED_ERROR;

            case SMIME_API_RETURNED_ERROR:
                return ENCRYPTED_ERROR;
        }
        throw new IllegalStateException("Unhandled case!");
    }

    @NonNull
    private static MessageCryptoDisplayStatus getDisplayStatusForPgpResult(CryptoResultAnnotation cryptoResult) {
        OpenPgpSignatureResult signatureResult = cryptoResult.getOpenPgpSignatureResult();
        OpenPgpDecryptionResult decryptionResult = cryptoResult.getOpenPgpDecryptionResult();
        if (decryptionResult == null || signatureResult == null) {
            throw new AssertionError("Both OpenPGP results must be non-null at this point!");
        }

        if (signatureResult.getResult() == OpenPgpSignatureResult.RESULT_NO_SIGNATURE &&
                cryptoResult.hasEncapsulatedResult()) {
            CryptoResultAnnotation encapsulatedResult = cryptoResult.getEncapsulatedResult();
            if (encapsulatedResult.isOpenPgpResult()) {
                signatureResult = encapsulatedResult.getOpenPgpSignatureResult();
                if (signatureResult == null) {
                    throw new AssertionError("OpenPGP must contain signature result at this point!");
                }
            }
        }

        switch (decryptionResult.getResult()) {
            case OpenPgpDecryptionResult.RESULT_NOT_ENCRYPTED:
                return getStatusForPgpUnencryptedResult(signatureResult);

            case OpenPgpDecryptionResult.RESULT_ENCRYPTED:
                return getStatusForPgpEncryptedResult(signatureResult);

            case OpenPgpDecryptionResult.RESULT_INSECURE:
                // TODO handle better?
                return ENCRYPTED_ERROR;
        }

        throw new AssertionError("all cases must be handled, this is a bug!");
    }

    @NonNull
    private static MessageCryptoDisplayStatus getStatusForPgpEncryptedResult(OpenPgpSignatureResult signatureResult) {
        switch (signatureResult.getResult()) {
            case OpenPgpSignatureResult.RESULT_NO_SIGNATURE:
                return ENCRYPTED_UNSIGNED;

            case OpenPgpSignatureResult.RESULT_VALID_KEY_CONFIRMED:
            case OpenPgpSignatureResult.RESULT_VALID_KEY_UNCONFIRMED:
                switch (signatureResult.getSenderResult()) {
                    case OpenPgpSignatureResult.SENDER_RESULT_UID_CONFIRMED:
                        return ENCRYPTED_SIGN_VERIFIED;
                    case OpenPgpSignatureResult.SENDER_RESULT_UID_UNCONFIRMED:
                        return ENCRYPTED_SIGN_UNVERIFIED;
                    case OpenPgpSignatureResult.SENDER_RESULT_UID_MISSING:
                        return ENCRYPTED_SIGN_MISMATCH;
                    case OpenPgpSignatureResult.SENDER_RESULT_NO_SENDER:
                        return ENCRYPTED_SIGN_UNVERIFIED;
                }
                throw new IllegalStateException("unhandled encrypted result case!");

            case OpenPgpSignatureResult.RESULT_KEY_MISSING:
                return ENCRYPTED_SIGN_UNKNOWN;

            case OpenPgpSignatureResult.RESULT_INVALID_SIGNATURE:
                return ENCRYPTED_SIGN_ERROR;

            case OpenPgpSignatureResult.RESULT_INVALID_KEY_EXPIRED:
                return ENCRYPTED_SIGN_EXPIRED;

            case OpenPgpSignatureResult.RESULT_INVALID_KEY_REVOKED:
                return ENCRYPTED_SIGN_REVOKED;

            case OpenPgpSignatureResult.RESULT_INVALID_KEY_INSECURE:
                return ENCRYPTED_SIGN_INSECURE;

            default:
                throw new IllegalStateException("unhandled encrypted result case!");
        }
    }

    @NonNull
    private static MessageCryptoDisplayStatus getStatusForPgpUnencryptedResult(OpenPgpSignatureResult signatureResult) {
        switch (signatureResult.getResult()) {
            case OpenPgpSignatureResult.RESULT_NO_SIGNATURE:
                return DISABLED;

            case OpenPgpSignatureResult.RESULT_VALID_KEY_CONFIRMED:
            case OpenPgpSignatureResult.RESULT_VALID_KEY_UNCONFIRMED:
                switch (signatureResult.getSenderResult()) {
                    case OpenPgpSignatureResult.SENDER_RESULT_UID_CONFIRMED:
                        return UNENCRYPTED_SIGN_VERIFIED;
                    case OpenPgpSignatureResult.SENDER_RESULT_UID_UNCONFIRMED:
                        return UNENCRYPTED_SIGN_UNVERIFIED;
                    case OpenPgpSignatureResult.SENDER_RESULT_UID_MISSING:
                        return UNENCRYPTED_SIGN_MISMATCH;
                    case OpenPgpSignatureResult.SENDER_RESULT_NO_SENDER:
                        return UNENCRYPTED_SIGN_UNVERIFIED;
                }
                throw new IllegalStateException("unhandled encrypted result case!");

            case OpenPgpSignatureResult.RESULT_KEY_MISSING:
                return UNENCRYPTED_SIGN_UNKNOWN;

            case OpenPgpSignatureResult.RESULT_INVALID_SIGNATURE:
                return UNENCRYPTED_SIGN_ERROR;

            case OpenPgpSignatureResult.RESULT_INVALID_KEY_EXPIRED:
                return UNENCRYPTED_SIGN_EXPIRED;

            case OpenPgpSignatureResult.RESULT_INVALID_KEY_REVOKED:
                return UNENCRYPTED_SIGN_REVOKED;

            case OpenPgpSignatureResult.RESULT_INVALID_KEY_INSECURE:
                return UNENCRYPTED_SIGN_INSECURE;

            default:
                throw new IllegalStateException("unhandled encrypted result case!");
        }
    }

    @NonNull
    private static MessageCryptoDisplayStatus getDisplayStatusForSMimeResult(CryptoResultAnnotation cryptoResult) {
        SMimeSignatureResult signatureResult = cryptoResult.getSMimeSignatureResult();
        SMimeDecryptionResult decryptionResult = cryptoResult.getSMimeDecryptionResult();
        if (decryptionResult == null || signatureResult == null) {
            throw new AssertionError("Both S/MIME results must be non-null at this point!");
        }

        if (signatureResult.getResult() == OpenPgpSignatureResult.RESULT_NO_SIGNATURE &&
                cryptoResult.hasEncapsulatedResult()) {
            CryptoResultAnnotation encapsulatedResult = cryptoResult.getEncapsulatedResult();
            if (encapsulatedResult.isOpenPgpResult()) {
                signatureResult = encapsulatedResult.getSMimeSignatureResult();
                if (signatureResult == null) {
                    throw new AssertionError("S/MIME must contain signature result at this point!");
                }
            }
        }

        switch (decryptionResult.getResult()) {
            case SMimeDecryptionResult.RESULT_NOT_ENCRYPTED:
                return getStatusForSMimeUnencryptedResult(signatureResult);

            case SMimeDecryptionResult.RESULT_ENCRYPTED:
                return getStatusForSMimeEncryptedResult(signatureResult);

            case SMimeDecryptionResult.RESULT_INSECURE:
                // TODO handle better?
                return ENCRYPTED_ERROR;
        }

        throw new AssertionError("all cases must be handled, this is a bug!");
    }

    @NonNull
    private static MessageCryptoDisplayStatus getStatusForSMimeEncryptedResult(SMimeSignatureResult signatureResult) {
        switch (signatureResult.getResult()) {
            case SMimeSignatureResult.RESULT_NO_SIGNATURE:
                return ENCRYPTED_UNSIGNED;

            case SMimeSignatureResult.RESULT_VALID_CERTIFICATE_CONFIRMED:
            case SMimeSignatureResult.RESULT_VALID_CERTIFICATE_UNCONFIRMED:
                switch (signatureResult.getSenderResult()) {
                    case SMimeSignatureResult.SENDER_RESULT_UID_CONFIRMED:
                        return ENCRYPTED_SIGN_VERIFIED;
                    case SMimeSignatureResult.SENDER_RESULT_UID_UNCONFIRMED:
                        return ENCRYPTED_SIGN_UNVERIFIED;
                    case SMimeSignatureResult.SENDER_RESULT_UID_MISSING:
                        return ENCRYPTED_SIGN_MISMATCH;
                    case SMimeSignatureResult.SENDER_RESULT_NO_SENDER:
                        return ENCRYPTED_SIGN_UNVERIFIED;
                }
                throw new IllegalStateException("unhandled encrypted result case!");

            case OpenPgpSignatureResult.RESULT_KEY_MISSING:
                return ENCRYPTED_SIGN_UNKNOWN;

            case OpenPgpSignatureResult.RESULT_INVALID_SIGNATURE:
                return ENCRYPTED_SIGN_ERROR;

            case OpenPgpSignatureResult.RESULT_INVALID_KEY_EXPIRED:
                return ENCRYPTED_SIGN_EXPIRED;

            case OpenPgpSignatureResult.RESULT_INVALID_KEY_REVOKED:
                return ENCRYPTED_SIGN_REVOKED;

            case OpenPgpSignatureResult.RESULT_INVALID_KEY_INSECURE:
                return ENCRYPTED_SIGN_INSECURE;

            default:
                throw new IllegalStateException("unhandled encrypted result case!");
        }
    }

    @NonNull
    private static MessageCryptoDisplayStatus getStatusForSMimeUnencryptedResult(SMimeSignatureResult signatureResult) {
        switch (signatureResult.getResult()) {
            case SMimeSignatureResult.RESULT_NO_SIGNATURE:
                return DISABLED;

            case SMimeSignatureResult.RESULT_VALID_CERTIFICATE_CONFIRMED:
            case SMimeSignatureResult.RESULT_VALID_CERTIFICATE_UNCONFIRMED:
                switch (signatureResult.getSenderResult()) {
                    case SMimeSignatureResult.SENDER_RESULT_UID_CONFIRMED:
                        return UNENCRYPTED_SIGN_VERIFIED;
                    case SMimeSignatureResult.SENDER_RESULT_UID_UNCONFIRMED:
                        return UNENCRYPTED_SIGN_UNVERIFIED;
                    case SMimeSignatureResult.SENDER_RESULT_UID_MISSING:
                        return UNENCRYPTED_SIGN_MISMATCH;
                    case SMimeSignatureResult.SENDER_RESULT_NO_SENDER:
                        return UNENCRYPTED_SIGN_UNVERIFIED;
                }
                throw new IllegalStateException("unhandled encrypted result case!");

            case SMimeSignatureResult.RESULT_KEY_MISSING:
                return UNENCRYPTED_SIGN_UNKNOWN;

            case SMimeSignatureResult.RESULT_INVALID_SIGNATURE:
                return UNENCRYPTED_SIGN_ERROR;

            case SMimeSignatureResult.RESULT_INVALID_CERTIFICATE_EXPIRED:
                return UNENCRYPTED_SIGN_EXPIRED;

            case SMimeSignatureResult.RESULT_INVALID_CERTIFICATE_REVOKED:
                return UNENCRYPTED_SIGN_REVOKED;

            case SMimeSignatureResult.RESULT_INVALID_INSECURE:
                return UNENCRYPTED_SIGN_INSECURE;

            default:
                throw new IllegalStateException("unhandled encrypted result case!");
        }
    }

    public boolean hasAssociatedKey() {
        switch (this) {
            case ENCRYPTED_SIGN_UNKNOWN:
            case ENCRYPTED_SIGN_VERIFIED:
            case ENCRYPTED_SIGN_UNVERIFIED:
            case ENCRYPTED_SIGN_MISMATCH:
            case ENCRYPTED_SIGN_EXPIRED:
            case ENCRYPTED_SIGN_REVOKED:
            case ENCRYPTED_SIGN_INSECURE:

            case UNENCRYPTED_SIGN_UNKNOWN:
            case UNENCRYPTED_SIGN_VERIFIED:
            case UNENCRYPTED_SIGN_UNVERIFIED:
            case UNENCRYPTED_SIGN_MISMATCH:
            case UNENCRYPTED_SIGN_EXPIRED:
            case UNENCRYPTED_SIGN_REVOKED:
            case UNENCRYPTED_SIGN_INSECURE:
                return true;
        }
        return false;
    }

}

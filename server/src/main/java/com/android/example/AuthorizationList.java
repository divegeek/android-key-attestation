/* Copyright 2019, The Android Open Source Project, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.example;

import static com.android.example.Constants.KM_TAG_ACTIVE_DATE_TIME;
import static com.android.example.Constants.KM_TAG_ALGORITHM;
import static com.android.example.Constants.KM_TAG_ALLOW_WHILE_ON_BODY;
import static com.android.example.Constants.KM_TAG_ALL_APPLICATIONS;
import static com.android.example.Constants.KM_TAG_APPLICATION_ID;
import static com.android.example.Constants.KM_TAG_ATTESTATION_APPLICATION_ID;
import static com.android.example.Constants.KM_TAG_ATTESTATION_ID_BRAND;
import static com.android.example.Constants.KM_TAG_ATTESTATION_ID_DEVICE;
import static com.android.example.Constants.KM_TAG_ATTESTATION_ID_IMEI;
import static com.android.example.Constants.KM_TAG_ATTESTATION_ID_MANUFACTURER;
import static com.android.example.Constants.KM_TAG_ATTESTATION_ID_MEID;
import static com.android.example.Constants.KM_TAG_ATTESTATION_ID_MODEL;
import static com.android.example.Constants.KM_TAG_ATTESTATION_ID_PRODUCT;
import static com.android.example.Constants.KM_TAG_ATTESTATION_ID_SERIAL;
import static com.android.example.Constants.KM_TAG_AUTH_TIMEOUT;
import static com.android.example.Constants.KM_TAG_BOOT_PATCH_LEVEL;
import static com.android.example.Constants.KM_TAG_CREATION_DATE_TIME;
import static com.android.example.Constants.KM_TAG_DIGEST;
import static com.android.example.Constants.KM_TAG_EC_CURVE;
import static com.android.example.Constants.KM_TAG_KEY_SIZE;
import static com.android.example.Constants.KM_TAG_NO_AUTH_REQUIRED;
import static com.android.example.Constants.KM_TAG_ORIGIN;
import static com.android.example.Constants.KM_TAG_ORIGINATION_EXPIRE_DATE_TIME;
import static com.android.example.Constants.KM_TAG_OS_PATCH_LEVEL;
import static com.android.example.Constants.KM_TAG_OS_VERSION;
import static com.android.example.Constants.KM_TAG_PADDING;
import static com.android.example.Constants.KM_TAG_PURPOSE;
import static com.android.example.Constants.KM_TAG_ROLLBACK_RESISTANCE;
import static com.android.example.Constants.KM_TAG_ROLLBACK_RESISTANT;
import static com.android.example.Constants.KM_TAG_ROOT_OF_TRUST;
import static com.android.example.Constants.KM_TAG_RSA_PUBLIC_EXPONENT;
import static com.android.example.Constants.KM_TAG_TRUSTED_CONFIRMATION_REQUIRED;
import static com.android.example.Constants.KM_TAG_TRUSTED_USER_PRESENCE_REQUIRED;
import static com.android.example.Constants.KM_TAG_UNLOCKED_DEVICE_REQUIRED;
import static com.android.example.Constants.KM_TAG_USAGE_EXPIRE_DATE_TIME;
import static com.android.example.Constants.KM_TAG_USER_AUTH_TYPE;
import static com.android.example.Constants.KM_TAG_VENDOR_PATCH_LEVEL;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.ASN1TaggedObject;

/**
 * This data structure contains the key pair's properties themselves, as defined in the Keymaster
 * hardware abstraction layer (HAL). You compare these values to the device's current state or to a
 * set of expected values to verify that a key pair is still valid for use in your app.
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
class AuthorizationList {

  final Optional<Set<Integer>> purpose;
  final Optional<Integer> algorithm;
  final Optional<Integer> keySize;
  final Optional<Set<Integer>> digest;
  final Optional<Set<Integer>> padding;
  final Optional<Integer> ecCurve;
  final Optional<Long> rsaPublicExponent;
  final boolean rollbackResistance;
  final Optional<Long> activeDateTime;
  final Optional<Long> originationExpireDateTime;
  final Optional<Long> usageExpireDateTime;
  final boolean noAuthRequired;
  final Optional<Integer> userAuthType;
  final Optional<Integer> authTimeout;
  final boolean allowWhileOnBody;
  final boolean trustedUserPresenceRequired;
  final boolean trustedConfirmationRequired;
  final boolean unlockedDeviceRequired;
  final boolean allApplications;
  final Optional<byte[]> applicationId;
  final Optional<Long> creationDateTime;
  final Optional<Integer> origin;
  final boolean rollbackResistant;
  final Optional<RootOfTrust> rootOfTrust;
  final Optional<Integer> osVersion;
  final Optional<Integer> osPatchLevel;
  final Optional<byte[]> attestationApplicationId;
  final Optional<byte[]> attestationIdBrand;
  final Optional<byte[]> attestationIdDevice;
  final Optional<byte[]> attestationIdProduct;
  final Optional<byte[]> attestationIdSerial;
  final Optional<byte[]> attestationIdImei;
  final Optional<byte[]> attestationIdMeid;
  final Optional<byte[]> attestationIdManufacturer;
  final Optional<byte[]> attestationIdModel;
  final Optional<Integer> vendorPatchLevel;
  final Optional<Integer> bootPatchLevel;

  private AuthorizationList(ASN1Encodable[] authorizationList) {
    Map<Integer, ASN1Primitive> authorizationMap = getAuthorizationMap(authorizationList);
    this.purpose = findOptionalIntegerSetAuthorizationListEntry(authorizationMap, KM_TAG_PURPOSE);
    this.algorithm =
        findOptionalIntegerAuthorizationListEntry(authorizationMap, (KM_TAG_ALGORITHM));
    this.keySize = findOptionalIntegerAuthorizationListEntry(authorizationMap, (KM_TAG_KEY_SIZE));
    this.digest = findOptionalIntegerSetAuthorizationListEntry(authorizationMap, KM_TAG_DIGEST);
    this.padding = findOptionalIntegerSetAuthorizationListEntry(authorizationMap, KM_TAG_PADDING);
    this.ecCurve = findOptionalIntegerAuthorizationListEntry(authorizationMap, (KM_TAG_EC_CURVE));
    this.rsaPublicExponent =
        findOptionalLongAuthorizationListEntry(authorizationMap, (KM_TAG_RSA_PUBLIC_EXPONENT));
    this.rollbackResistance =
        findBooleanAuthorizationListEntry(authorizationMap, KM_TAG_ROLLBACK_RESISTANCE);
    this.activeDateTime =
        findOptionalLongAuthorizationListEntry(authorizationMap, (KM_TAG_ACTIVE_DATE_TIME));
    this.originationExpireDateTime =
        findOptionalLongAuthorizationListEntry(
            authorizationMap, (KM_TAG_ORIGINATION_EXPIRE_DATE_TIME));
    this.usageExpireDateTime =
        findOptionalLongAuthorizationListEntry(
            authorizationMap, (KM_TAG_USAGE_EXPIRE_DATE_TIME));
    this.noAuthRequired =
        findBooleanAuthorizationListEntry(authorizationMap, KM_TAG_NO_AUTH_REQUIRED);
    this.userAuthType =
        findOptionalIntegerAuthorizationListEntry(authorizationMap, (KM_TAG_USER_AUTH_TYPE));
    this.authTimeout =
        findOptionalIntegerAuthorizationListEntry(authorizationMap, (KM_TAG_AUTH_TIMEOUT));
    this.allowWhileOnBody =
        findBooleanAuthorizationListEntry(authorizationMap, KM_TAG_ALLOW_WHILE_ON_BODY);
    this.trustedUserPresenceRequired =
        findBooleanAuthorizationListEntry(authorizationMap, KM_TAG_TRUSTED_USER_PRESENCE_REQUIRED);
    this.trustedConfirmationRequired =
        findBooleanAuthorizationListEntry(authorizationMap, KM_TAG_TRUSTED_CONFIRMATION_REQUIRED);
    this.unlockedDeviceRequired =
        findBooleanAuthorizationListEntry(authorizationMap, KM_TAG_UNLOCKED_DEVICE_REQUIRED);
    this.allApplications =
        findBooleanAuthorizationListEntry(authorizationMap, KM_TAG_ALL_APPLICATIONS);
    this.applicationId =
        findOptionalByteArrayAuthorizationListEntry(authorizationMap, (KM_TAG_APPLICATION_ID));
    this.creationDateTime =
        findOptionalLongAuthorizationListEntry(authorizationMap, (KM_TAG_CREATION_DATE_TIME));
    this.origin = findOptionalIntegerAuthorizationListEntry(authorizationMap, (KM_TAG_ORIGIN));
    this.rollbackResistant =
        findBooleanAuthorizationListEntry(authorizationMap, KM_TAG_ROLLBACK_RESISTANT);
    this.rootOfTrust =
        Optional.ofNullable(
            RootOfTrust.createRootOfTrust(
                (ASN1Sequence)
                    findAuthorizationListEntry(authorizationMap, KM_TAG_ROOT_OF_TRUST)));
    this.osVersion =
        findOptionalIntegerAuthorizationListEntry(authorizationMap, (KM_TAG_OS_VERSION));
    this.osPatchLevel =
        findOptionalIntegerAuthorizationListEntry(authorizationMap, (KM_TAG_OS_PATCH_LEVEL));
    this.attestationApplicationId =
        findOptionalByteArrayAuthorizationListEntry(
            authorizationMap, (KM_TAG_ATTESTATION_APPLICATION_ID));
    this.attestationIdBrand =
        findOptionalByteArrayAuthorizationListEntry(
            authorizationMap, (KM_TAG_ATTESTATION_ID_BRAND));
    this.attestationIdDevice =
        findOptionalByteArrayAuthorizationListEntry(
            authorizationMap, (KM_TAG_ATTESTATION_ID_DEVICE));
    this.attestationIdProduct =
        findOptionalByteArrayAuthorizationListEntry(
            authorizationMap, (KM_TAG_ATTESTATION_ID_PRODUCT));
    this.attestationIdSerial =
        findOptionalByteArrayAuthorizationListEntry(
            authorizationMap, (KM_TAG_ATTESTATION_ID_SERIAL));
    this.attestationIdImei =
        findOptionalByteArrayAuthorizationListEntry(
            authorizationMap, (KM_TAG_ATTESTATION_ID_IMEI));
    this.attestationIdMeid =
        findOptionalByteArrayAuthorizationListEntry(
            authorizationMap, (KM_TAG_ATTESTATION_ID_MEID));
    this.attestationIdManufacturer =
        findOptionalByteArrayAuthorizationListEntry(
            authorizationMap, (KM_TAG_ATTESTATION_ID_MANUFACTURER));
    this.attestationIdModel =
        findOptionalByteArrayAuthorizationListEntry(
            authorizationMap, (KM_TAG_ATTESTATION_ID_MODEL));
    this.vendorPatchLevel =
        findOptionalIntegerAuthorizationListEntry(authorizationMap, (KM_TAG_VENDOR_PATCH_LEVEL));
    this.bootPatchLevel =
        findOptionalIntegerAuthorizationListEntry(authorizationMap, (KM_TAG_BOOT_PATCH_LEVEL));
  }

  static AuthorizationList createAuthorizationList(
      ASN1Encodable[] authorizationList) {
    return new AuthorizationList(authorizationList);
  }

  private static Map<Integer, ASN1Primitive> getAuthorizationMap(
      ASN1Encodable[] authorizationList) {
    Map<Integer, ASN1Primitive> authorizationMap = new HashMap<>();
    for (ASN1Encodable entry : authorizationList) {
      ASN1TaggedObject taggedEntry = (ASN1TaggedObject) entry;
      authorizationMap.put(taggedEntry.getTagNo(), taggedEntry.getObject());
    }
    return authorizationMap;
  }

  private static ASN1Primitive findAuthorizationListEntry(
      Map<Integer, ASN1Primitive> authorizationMap, int tag) {
    return authorizationMap.getOrDefault(tag, null);
  }

  private static Optional<Set<Integer>> findOptionalIntegerSetAuthorizationListEntry(
      Map<Integer, ASN1Primitive> authorizationMap, int tag) {
    ASN1Set asn1Set = (ASN1Set) findAuthorizationListEntry(authorizationMap, tag);
    if (asn1Set == null) {
      return Optional.empty();
    }
    Set<Integer> entrySet = new HashSet<>();
    for (ASN1Encodable value : asn1Set) {
      entrySet.add(ASN1Parsing.getIntegerFromAsn1(value));
    }
    return Optional.of(entrySet);
  }

  private static Optional<Integer> findOptionalIntegerAuthorizationListEntry(
      Map<Integer, ASN1Primitive> authorizationMap, int tag) {
    ASN1Primitive entry = findAuthorizationListEntry(authorizationMap, tag);
    if (entry == null) {
      return Optional.empty();
    }
    return Optional.of(ASN1Parsing.getIntegerFromAsn1(entry));
  }

  private static Optional<Long> findOptionalLongAuthorizationListEntry(
      Map<Integer, ASN1Primitive> authorizationMap, int tag) {
    ASN1Integer longEntry = ((ASN1Integer) findAuthorizationListEntry(authorizationMap, tag));
    if (longEntry == null) {
      return Optional.empty();
    }
    return Optional.of(longEntry.getValue().longValue());
  }

  private static boolean findBooleanAuthorizationListEntry(
      Map<Integer, ASN1Primitive> authorizationMap, int tag) {
    return null != findAuthorizationListEntry(authorizationMap, tag);
  }

  private static Optional<byte[]> findOptionalByteArrayAuthorizationListEntry(
      Map<Integer, ASN1Primitive> authorizationMap, int tag) {
    ASN1OctetString entry = (ASN1OctetString) findAuthorizationListEntry(authorizationMap, tag);
    if (entry == null) {
      return Optional.empty();
    }
    return Optional.of(entry.getOctets());
  }
}

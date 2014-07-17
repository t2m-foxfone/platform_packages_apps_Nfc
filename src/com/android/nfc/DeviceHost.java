/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/******************************************************************************
 *
 *  The original Work has been changed by NXP Semiconductors.
 *
 *  Copyright (C) 2013-2014 NXP Semiconductors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 ******************************************************************************/
package com.android.nfc;

import android.nfc.NdefMessage;
import android.os.Bundle;

import java.io.IOException;

public interface DeviceHost {
    public interface DeviceHostListener {
        public void onRemoteEndpointDiscovered(TagEndpoint tag);

        /**
         * Notifies transaction
         */
        public void onCardEmulationDeselected();

        /**
         * Notifies transaction
         */
        public void onCardEmulationAidSelected(byte[] aid,byte[] data, int evtSrc);

        /**
         * Notifies connectivity event from the SE
         */
        public void onConnectivityEvent(int evtSrc);

        /**
         * Notifies about multiple card presented to
         * emvco reader.
         */
        public void onEmvcoMultiCardDetectedEvent();

        /**
         */
        public void onHostCardEmulationActivated();
        public void onHostCardEmulationData(byte[] data);
        public void onHostCardEmulationDeactivated();
        public void onAidRoutingTableFull();

        /**
         * Notifies P2P Device detected, to activate LLCP link
         */
        public void onLlcpLinkActivated(NfcDepEndpoint device);

        /**
         * Notifies P2P Device detected, to activate LLCP link
         */
        public void onLlcpLinkDeactivated(NfcDepEndpoint device);

        public void onLlcpFirstPacketReceived(NfcDepEndpoint device);

        public void onRemoteFieldActivated();

        public void onRemoteFieldDeactivated();

        /**
         * Notifies that the SE has been activated in listen mode
         */
        public void onSeListenActivated();

        /**
         * Notifies that the SE has been deactivated
         */
        public void onSeListenDeactivated();

        public void onSeApduReceived(byte[] apdu);

        public void onSeEmvCardRemoval();

        public void onSeMifareAccess(byte[] block);
        /**
         * Notifies SWP Reader Events.
         */
        public void onSWPReaderRequestedEvent(boolean istechA, boolean istechB);

        public void onSWPReaderActivatedEvent();

        public void onSWPReaderDeActivatedEvent();
    }

    public interface TagEndpoint {
        boolean connect(int technology);
        boolean reconnect();
        boolean disconnect();

        boolean presenceCheck();
        boolean isPresent();
        void startPresenceChecking(int presenceCheckDelay);

        int[] getTechList();
        void removeTechnology(int tech); // TODO remove this one
        Bundle[] getTechExtras();
        byte[] getUid();
        int getHandle();

        byte[] transceive(byte[] data, boolean raw, int[] returnCode);

        boolean checkNdef(int[] out);
        byte[] readNdef();
        boolean writeNdef(byte[] data);
        NdefMessage findAndReadNdef();
        boolean formatNdef(byte[] key);
        boolean isNdefFormatable();
        boolean makeReadOnly();

        int getConnectedTechnology();
    }

    public interface NfceeEndpoint {
        // TODO flesh out multi-EE and use this
    }

    public interface NfcDepEndpoint {

        /**
         * Peer-to-Peer Target
         */
        public static final short MODE_P2P_TARGET = 0x00;
        /**
         * Peer-to-Peer Initiator
         */
        public static final short MODE_P2P_INITIATOR = 0x01;
        /**
         * Invalid target mode
         */
        public static final short MODE_INVALID = 0xff;

        public byte[] receive();

        public boolean send(byte[] data);

        public boolean connect();

        public boolean disconnect();

        public byte[] transceive(byte[] data);

        public int getHandle();

        public int getMode();

        public byte[] getGeneralBytes();
    }

    public interface LlcpSocket {
        public void connectToSap(int sap) throws IOException;

        public void connectToService(String serviceName) throws IOException;

        public void close() throws IOException;

        public void send(byte[] data) throws IOException;

        public int receive(byte[] recvBuff) throws IOException;

        public int getRemoteMiu();

        public int getRemoteRw();

        public int getLocalSap();

        public int getLocalMiu();

        public int getLocalRw();
    }

    public interface LlcpServerSocket {
        public LlcpSocket accept() throws IOException, LlcpException;

        public void close() throws IOException;
    }

    public interface LlcpConnectionlessSocket {
        public int getLinkMiu();

        public int getSap();

        public void send(int sap, byte[] data) throws IOException;

        public LlcpPacket receive() throws IOException;

        public void close() throws IOException;
    }

    /**
     * Called at boot if NFC is disabled to give the device host an opportunity
     * to check the firmware version to see if it needs updating. Normally the firmware version
     * is checked during {@link #initialize()}, but the firmware may need to be updated after
     * an OTA update.
     *
     * <p>This is called from a thread
     * that may block for long periods of time during the update process.
     */
    public void checkFirmware();

    public boolean download();

    public boolean initialize();

    public int GetDefaultSE();

    public boolean deinitialize();

    public String getName();

    public void enableDiscovery();

    public void doSetScreenState(int mScreenState);

    public void disableDiscovery();

    public void enableRoutingToHost();

    public void disableRoutingToHost();

    public int[] doGetSecureElementList();

    public void doSelectSecureElement(int seID);
    public void doSetSecureElementListenTechMask(int tech_mask);
    public int doGetSecureElementTechList();
    public void doDeselectSecureElement(int seID);

    public void doSetSEPowerOffState(int seID,boolean enable);

    public void setDefaultTechRoute(int seID, int tech_switchon, int tech_switchoff);

    public void setDefaultProtoRoute(int seID, int proto_switchon, int proto_switchoff);


    public boolean sendRawFrame(byte[] data);

    public boolean routeAid(byte[] aid, int route, int powerState);
    public boolean setDefaultRoute(int defaultRouteEntry, int defaultProtoRouteEntry, int defaultTechRouteEntry);
    public void clearRouting();

    public int setEmvCoPollProfile(boolean enable, int route);
    public int getAidTableSize();

    public int getDefaultAidRoute();

    public int getDefaultDesfireRoute();

    public int  getDefaultMifareCLTRoute();


    public LlcpConnectionlessSocket createLlcpConnectionlessSocket(int nSap, String sn)
            throws LlcpException;

    public LlcpServerSocket createLlcpServerSocket(int nSap, String sn, int miu,
            int rw, int linearBufferLength) throws LlcpException;

    public LlcpSocket createLlcpSocket(int sap, int miu, int rw,
            int linearBufferLength) throws LlcpException;

    public boolean doCheckLlcp();

    public boolean doActivateLlcp();

    public void resetTimeouts();

    public boolean setTimeout(int technology, int timeout);

    public int getTimeout(int technology);

    public void doAbort();

    boolean canMakeReadOnly(int technology);

    int getMaxTransceiveLength(int technology);

    void setP2pInitiatorModes(int modes);

    void setP2pTargetModes(int modes);

    boolean getExtendedLengthApdusSupported();

    boolean enablePN544Quirks();

    byte[][] getWipeApdus();

    int getDefaultLlcpMiu();

    int getDefaultLlcpRwSize();

    int getChipVer();

    int JCOSDownload();

    void doSetVenConfigValue(int VenConfig);

    String dump();

    boolean enableReaderMode(int technologies);

    boolean disableReaderMode();

    boolean SetScrnState(int Enable);

    //Factory Test --start
    public void doPrbsOn(int tech, int rate);

    public void doPrbsOff();

    public int SWPSelfTest(int ch);

    public int getFWVersion();

    public void doSetEEPROM(byte[] val);
    //Factory Test --end

}

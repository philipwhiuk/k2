/*
 * The MIT License
 * Copyright (c) 2012 Microsoft Corporation
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package microsoft.exchange.webservices.data.core;

import microsoft.exchange.webservices.data.core.enumeration.misc.XmlNamespace;
import microsoft.exchange.webservices.data.core.exception.service.local.ServiceXmlSerializationException;
import microsoft.exchange.webservices.data.misc.OutParam;
import microsoft.exchange.webservices.data.property.complex.ISearchStringProvider;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

import org.xmlpull.v1.XmlSerializer;
import timber.log.Timber;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import android.util.Base64;
import android.util.Xml;


/**
 * Stax based XML Writer implementation.
 */
public class EwsServiceXmlWriter implements IDisposable {

  /**
   * The is disposed.
   */
  private boolean isDisposed;

  /**
   * The service.
   */
  private ExchangeServiceBase service;

  /**
   * The xml writer.
   */
  private XmlSerializer xmlWriter;

  /**
   * The is time zone header emitted.
   */
  private boolean isTimeZoneHeaderEmitted;

  /**
   * The Buffer size.
   */
  private static final int BufferSize = 4096;

  /**
   * The  requireWSSecurityUtilityNamespace *
   */

  protected boolean requireWSSecurityUtilityNamespace;

  /**
   * Initializes a new instance.
   *
   * @param service the service
   * @param stream the stream
   */
  public EwsServiceXmlWriter(ExchangeServiceBase service, OutputStream stream) throws IOException {
    this.service = service;
    xmlWriter = Xml.newSerializer();
    xmlWriter.setOutput(stream, "UTF-8");
  }

  /**
   * Try to convert object to a string.
   *
   * @param value The value.
   * @param str   the str
   * @return True if object was converted, false otherwise. A null object will
   * be "successfully" converted to a null string.
   */
  protected boolean tryConvertObjectToString(Object value,
      OutParam<String> str) {
    boolean converted = true;
    str.setParam(null);
    if (value != null) {
      if (value.getClass().isEnum()) {
        str.setParam(EwsUtilities.serializeEnum(value));
      } else if (value.getClass().equals(Boolean.class)) {
        str.setParam(EwsUtilities.boolToXSBool((Boolean) value));
      } else if (value instanceof Date) {
        str
            .setParam(this.service
                .convertDateTimeToUniversalDateTimeString(
                    (Date) value));
      } else if (value.getClass().isPrimitive()) {
        str.setParam(value.toString());
      } else if (value instanceof String) {
        str.setParam(value.toString());
      } else if (value instanceof ISearchStringProvider) {
        ISearchStringProvider searchStringProvider =
            (ISearchStringProvider) value;
        str.setParam(searchStringProvider.getSearchString());
      } else if (value instanceof Number) {
        str.setParam(value.toString());
      } else {
        converted = false;
      }
    }
    return converted;
  }

  /**
   * Performs application-defined tasks associated with freeing, releasing, or
   * resetting unmanaged resources.
   */
  @Override
  public void dispose() {
    if (!this.isDisposed) {
      //TODO: ??
      this.isDisposed = true;
    }
  }

  /**
   * Flushes this instance.
   *
   * @throws IOException the XML stream exception
   */
  public void flush() throws IOException {
    this.xmlWriter.flush();
  }

  /**
   * Writes the start element.
   *
   * @param xmlNamespace the XML namespace
   * @param localName    the local name of the element
   * @throws IOException the XML stream exception
   */
  public void writeStartElement(XmlNamespace xmlNamespace, String localName)
          throws IOException {
    String strPrefix = EwsUtilities.getNamespacePrefix(xmlNamespace);
    String strNameSpace = EwsUtilities.getNamespaceUri(xmlNamespace);
    xmlWriter.startTag(strPrefix, localName);
  }

  /**
   * Writes the end element.
   *
   * @throws IOException the XML stream exception
   */
  public void writeEndElement(XmlNamespace xmlNamespace, String name) throws IOException {
    String strPrefix = EwsUtilities.getNamespacePrefix(xmlNamespace);
    this.xmlWriter.endTag(strPrefix, name);
  }

  /**
   * Writes the attribute value.
   *
   * @param localName the local name of the attribute
   * @param value     the value
   * @throws ServiceXmlSerializationException the service xml serialization exception
   */
  public void writeAttributeValue(String localName, Object value)
      throws ServiceXmlSerializationException {
    this.writeAttributeValue(localName,
        false /* alwaysWriteEmptyString */, value);
  }

  /**
   * Writes the attribute value.  Optionally emits empty string values.
   *
   * @param localName              the local name of the attribute.
   * @param alwaysWriteEmptyString always emit the empty string as the value.
   * @param value                  the value
   * @throws ServiceXmlSerializationException the service xml serialization exception
   */
  public void writeAttributeValue(String localName,
      boolean alwaysWriteEmptyString,
      Object value) throws ServiceXmlSerializationException {
    OutParam<String> stringOut = new OutParam<String>();
    String stringValue = null;
    if (this.tryConvertObjectToString(value, stringOut)) {
      stringValue = stringOut.getParam();
      if ((null != stringValue) && (alwaysWriteEmptyString || (stringValue.length() != 0))) {
        this.writeAttributeString(localName, stringValue);
      }
    } else {
      throw new ServiceXmlSerializationException(String.format(
          "Values of type '%s' can't be used for the '%s' attribute.", value.getClass()
              .getName(), localName));
    }
  }

  /**
   * Writes the attribute value.
   *
   * @param namespacePrefix the namespace prefix
   * @param localName       the local name of the attribute
   * @param value           the value
   * @throws ServiceXmlSerializationException the service xml serialization exception
   */
  public void writeAttributeValue(String namespacePrefix, String localName,
      Object value) throws ServiceXmlSerializationException {
    OutParam<String> stringOut = new OutParam<String>();
    String stringValue = null;
    if (this.tryConvertObjectToString(value, stringOut)) {
      stringValue = stringOut.getParam();
      if (null != stringValue && !stringValue.isEmpty()) {
        this.writeAttributeString(namespacePrefix, localName,
            stringValue);
      }
    } else {
      throw new ServiceXmlSerializationException(String.format(
          "Values of type '%s' can't be used for the '%s' attribute.", value.getClass()
              .getName(), localName));
    }
  }

  /**
   * Writes the attribute value.
   *
   * @param localName   The local name of the attribute.
   * @param stringValue The string value.
   * @throws ServiceXmlSerializationException Thrown if string value isn't valid for XML
   */
  protected void writeAttributeString(String localName, String stringValue)
      throws ServiceXmlSerializationException {
    try {
      this.xmlWriter.attribute(null, localName, stringValue);
    } catch (IOException e) {
      // Bug E14:65046: XmlTextWriter will throw ArgumentException
      //if string includes invalid characters.
      throw new ServiceXmlSerializationException(String.format(
          "The invalid value '%s' was specified for the '%s' attribute.", stringValue, localName), e);
    }
  }

  /**
   * Writes the attribute value.
   *
   * @param namespacePrefix The namespace prefix.
   * @param localName       The local name of the attribute.
   * @param stringValue     The string value.
   * @throws ServiceXmlSerializationException Thrown if string value isn't valid for XML.
   */
  protected void writeAttributeString(String namespacePrefix,
      String localName, String stringValue)
      throws ServiceXmlSerializationException {
    try {
      this.xmlWriter.attribute(namespacePrefix, localName,
          stringValue);
    } catch (IOException e) {
      // Bug E14:65046: XmlTextWriter will throw ArgumentException
      //if string includes invalid characters.
      throw new ServiceXmlSerializationException(String.format(
          "The invalid value '%s' was specified for the '%s' attribute.", stringValue, localName), e);
    }
  }

  /**
   * Writes string value.
   *
   * @param value The value.
   * @param name  Element name (used for error handling)
   * @throws ServiceXmlSerializationException Thrown if string value isn't valid for XML.
   */
  public void writeValue(String value, String name)
      throws ServiceXmlSerializationException {
    try {
      this.xmlWriter.text(value);
    } catch (IOException e) {
      // Bug E14:65046: XmlTextWriter will throw ArgumentException
      //if string includes invalid characters.
      throw new ServiceXmlSerializationException(String.format(
          "The invalid value '%s' was specified for the '%s' element.", value, name), e);
    }
  }

  /**
   * Writes the element value.
   *
   * @param xmlNamespace the XML namespace
   * @param localName    the local name of the element
   * @param displayName  the name that should appear in the exception message when the value can not be serialized
   * @param value        the value
   * @throws IOException the XML stream exception
   * @throws ServiceXmlSerializationException the service xml serialization exception
   */
  public void writeElementValue(XmlNamespace xmlNamespace, String localName, String displayName, Object value)
      throws IOException, ServiceXmlSerializationException {
    String stringValue = null;
    OutParam<String> strOut = new OutParam<String>();

    if (this.tryConvertObjectToString(value, strOut)) {
      stringValue = strOut.getParam();
      if (null != stringValue) {
        // allow an empty string to create an empty element (like <Value
        // />).
        this.writeStartElement(xmlNamespace, localName);
        this.writeValue(stringValue, displayName);
        this.writeEndElement(xmlNamespace, localName);
      }
    } else {
      throw new ServiceXmlSerializationException(String.format(
          "Values of type '%s' can't be used for the '%s' element.", value.getClass()
              .getName(), localName));
    }
  }

  public void writeNode(Node xmlNode) throws IOException {
    if (xmlNode != null) {
      writeNode(xmlNode, this.xmlWriter);
    }
  }

  /**
   * @param xmlNode XML node
   * @param xmlStreamWriter XML stream writer
   * @throws IOException the XML stream exception
   */
  public static void writeNode(Node xmlNode, XmlSerializer xmlStreamWriter)
      throws IOException {
    if (xmlNode instanceof Element) {
      addElement((Element) xmlNode, xmlStreamWriter);
    } else if (xmlNode instanceof Text) {
      xmlStreamWriter.text(xmlNode.getNodeValue());
    } else if (xmlNode instanceof CDATASection) {
      xmlStreamWriter.cdsect(((CDATASection) xmlNode).getData());
    } else if (xmlNode instanceof Comment) {
      xmlStreamWriter.comment(((Comment) xmlNode).getData());
    } else if (xmlNode instanceof EntityReference) {
      xmlStreamWriter.entityRef(xmlNode.getNodeValue());
    } else if (xmlNode instanceof ProcessingInstruction) {
      ProcessingInstruction procInst = (ProcessingInstruction) xmlNode;
      xmlStreamWriter.processingInstruction(procInst.getData());
    } else if (xmlNode instanceof Document) {
      writeToDocument((Document) xmlNode, xmlStreamWriter);
    }
  }

  /**
   * @param document XML document
   * @param xmlStreamWriter XML stream writer
   * @throws IOException the XML stream exception
   */
  public static void writeToDocument(Document document,
      XmlSerializer xmlStreamWriter) throws IOException {

    xmlStreamWriter.startDocument(null, false);
    Element rootElement = document.getDocumentElement();
    addElement(rootElement, xmlStreamWriter);
    xmlStreamWriter.endDocument();
  }

  /**
   * @param element DOM element
   * @param writer XML stream writer
   * @throws IOException the XML stream exception
   */
  public static void addElement(Element element, XmlSerializer writer)
      throws IOException {
    String nameSpace = element.getNamespaceURI();
    String prefix = element.getPrefix();
    String localName = element.getLocalName();
    if (prefix == null) {
      prefix = "";
    }
    if (localName == null) {
      localName = element.getNodeName();

      if (localName == null) {
        throw new IllegalStateException(
            "Element's local name cannot be null!");
      }
    }

    String decUri = writer.getNamespace();
    boolean declareNamespace = decUri == null || !decUri.equals(nameSpace);

    if (nameSpace == null || nameSpace.length() == 0) {
      writer.startTag(null, localName);
    } else {
      writer.startTag(prefix, nameSpace);
    }

    NamedNodeMap attrs = element.getAttributes();
    for (int i = 0; i < attrs.getLength(); i++) {
      Node attr = attrs.item(i);

      String name = attr.getNodeName();
      String attrPrefix = "";
      int prefixIndex = name.indexOf(':');
      if (prefixIndex != -1) {
        attrPrefix = name.substring(0, prefixIndex);
        name = name.substring(prefixIndex + 1);
      }

      writer.attribute(attrPrefix, name, attr.getNodeValue());
    }

    NodeList nodes = element.getChildNodes();
    for (int i = 0; i < nodes.getLength(); i++) {
      Node n = nodes.item(i);
      writeNode(n, writer);
    }

    if (nameSpace == null || nameSpace.length() == 0) {
      writer.endTag(null, localName);
    } else {
      writer.endTag(prefix, nameSpace);
    }
  }



  /**
   * Writes the element value.
   *
   * @param xmlNamespace the XML namespace
   * @param localName    the local name of the element
   * @param value        the value
   * @throws IOException the XML stream exception
   * @throws ServiceXmlSerializationException the service xml serialization exception
   */
  public void writeElementValue(XmlNamespace xmlNamespace, String localName,
      Object value) throws IOException,
      ServiceXmlSerializationException {
    this.writeElementValue(xmlNamespace, localName, localName, value);
  }

  /**
   * Writes the base64-encoded element value.
   *
   * @param buffer the buffer
   * @throws IOException the XML stream exception
   */
  public void writeBase64ElementValue(byte[] buffer)
      throws IOException {

    String strValue = android.util.Base64.encodeToString(buffer, 0);
    this.xmlWriter.text(strValue);//Base64.encode(buffer));
  }

  /**
   * Writes the base64-encoded element value.
   *
   * @param stream the stream
   * @throws IOException signals that an I/O exception has occurred
   * @throws IOException the XML stream exception
   */
  public void writeBase64ElementValue(InputStream stream) throws IOException,
          IOException {

    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    byte[] buf = new byte[BufferSize];
    try {
      for (int readNum; (readNum = stream.read(buf)) != -1; ) {
        bos.write(buf, 0, readNum);
      }
    } catch (IOException ex) {
      Timber.e(ex);
    } finally {
      bos.close();
    }
    byte[] bytes = bos.toByteArray();
    String strValue = Base64.encodeToString(bytes, 0);
    this.xmlWriter.text(strValue);

  }

  /**
   * Gets the internal XML writer.
   *
   * @return the internal writer
   */
  public XmlSerializer getInternalWriter() {
    return xmlWriter;
  }

  /**
   * Gets the service.
   *
   * @return The service.
   */
  public ExchangeServiceBase getService() {
    return service;
  }

  /**
   * Gets a value indicating whether the SOAP message need WSSecurity Utility namespace.
   */
  public boolean isRequireWSSecurityUtilityNamespace() {
    return requireWSSecurityUtilityNamespace;
  }

  /**
   * Sets a value indicating whether the SOAP message need WSSecurity Utility namespace.
   */
  public void setRequireWSSecurityUtilityNamespace(boolean requireWSSecurityUtilityNamespace) {
    this.requireWSSecurityUtilityNamespace = requireWSSecurityUtilityNamespace;
  }

  /**
   * Gets a value indicating whether the time zone SOAP header was emitted
   * through this writer.
   *
   * @return true if the time zone SOAP header was emitted; otherwise false.
   */
  public boolean isTimeZoneHeaderEmitted() {
    return isTimeZoneHeaderEmitted;
  }

  /**
   * Sets a value indicating whether the time zone SOAP header was emitted
   * through this writer.
   *
   * @param isTimeZoneHeaderEmitted true if the time zone SOAP header was emitted; otherwise
   *                                false.
   */
  public void setTimeZoneHeaderEmitted(boolean isTimeZoneHeaderEmitted) {
    this.isTimeZoneHeaderEmitted = isTimeZoneHeaderEmitted;
  }

  /**
   * Write start document.
   *
   * @throws IOException the XML stream exception
   */
  public void writeStartDocument() throws IOException {
    this.xmlWriter.startDocument("utf-8", false);
  }
}
